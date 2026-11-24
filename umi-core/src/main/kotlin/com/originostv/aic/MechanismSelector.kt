package com.originostv.aic

/** Failure location in the task execution pipeline. */
enum class FailureStage {
    INPUT, NETWORK, PROTOCOL, PARSING, RESOLUTION, DECODING, RENDERING, RESOURCE, UNKNOWN
}

/** Increasing levels represent increasingly disruptive changes. */
enum class AdaptationLevel { PARAMETER, STRATEGY, IMPLEMENTATION, ARCHITECTURE }

data class FailureEvidence(
    val stage: FailureStage,
    val failureCode: String,
    val consecutiveFailures: Int,
    val confidence: Double
)

data class MechanismCandidate(
    val id: String,
    val level: AdaptationLevel,
    val supportedStages: Set<FailureStage>,
    val riskTier: Int,
    val capabilityScore: Double,
    val reversible: Boolean
)

data class AdaptationPolicy(
    val maxAutomaticRiskTier: Int = 1,
    val minimumConfidence: Double = 0.75,
    val minimumRepeatedFailures: Int = 2,
    val allowArchitectureChanges: Boolean = false
)

sealed interface MechanismSelection {
    data class Selected(
        val candidate: MechanismCandidate,
        val requiresApproval: Boolean,
        val reason: String
    ) : MechanismSelection

    data class NoSafeCandidate(val reason: String) : MechanismSelection
}

/**
 * Deterministic, side-effect-free first-stage AIC selector.
 * It chooses a mechanism; it does not execute, deploy, or mutate runtime state.
 */
class MechanismSelector {
    fun select(
        evidence: FailureEvidence,
        candidates: List<MechanismCandidate>,
        policy: AdaptationPolicy = AdaptationPolicy()
    ): MechanismSelection {
        if (evidence.stage == FailureStage.UNKNOWN) {
            return MechanismSelection.NoSafeCandidate("Unknown failure stage requires further diagnosis")
        }
        if (evidence.confidence < policy.minimumConfidence) {
            return MechanismSelection.NoSafeCandidate("Diagnostic confidence is below the automatic decision threshold")
        }
        if (evidence.consecutiveFailures < policy.minimumRepeatedFailures) {
            return MechanismSelection.NoSafeCandidate("Insufficient repeated evidence; collect more observations")
        }

        val compatible = candidates.filter {
            evidence.stage in it.supportedStages &&
                it.riskTier in 0..3 &&
                it.capabilityScore in 0.0..1.0 &&
                (policy.allowArchitectureChanges || it.level != AdaptationLevel.ARCHITECTURE)
        }
        if (compatible.isEmpty()) {
            return MechanismSelection.NoSafeCandidate("No candidate supports this failure stage within the declared policy")
        }

        val automatic = compatible.filter {
            it.riskTier <= policy.maxAutomaticRiskTier && it.reversible
        }
        val pool = if (automatic.isNotEmpty()) automatic else compatible
        val selected = pool.sortedWith(
            compareBy<MechanismCandidate> { it.level.ordinal }
                .thenByDescending { it.capabilityScore }
                .thenBy { it.riskTier }
                .thenBy { it.id }
        ).first()

        val approval = !selected.reversible ||
            selected.riskTier > policy.maxAutomaticRiskTier ||
            selected.level == AdaptationLevel.ARCHITECTURE
        val reason = if (approval) {
            "Best compatible candidate requires explicit approval due to risk or change scope"
        } else {
            "Selected the least disruptive compatible reversible mechanism"
        }
        return MechanismSelection.Selected(selected, approval, reason)
    }
}
