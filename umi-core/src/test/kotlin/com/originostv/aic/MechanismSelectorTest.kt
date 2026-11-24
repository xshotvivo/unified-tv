package com.originostv.aic

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MechanismSelectorTest {
    private val evidence = FailureEvidence(
        stage = FailureStage.PARSING,
        failureCode = "PARSER_REJECTED_INPUT",
        consecutiveFailures = 3,
        confidence = 0.92
    )

    @Test
    fun selectsLeastDisruptiveCompatibleMechanism() {
        val result = MechanismSelector().select(
            evidence,
            listOf(
                MechanismCandidate("new-parser", AdaptationLevel.IMPLEMENTATION, setOf(FailureStage.PARSING), 1, 0.95, true),
                MechanismCandidate("fallback-strategy", AdaptationLevel.STRATEGY, setOf(FailureStage.PARSING), 1, 0.80, true)
            )
        )
        val selected = assertIs<MechanismSelection.Selected>(result)
        assertEquals("fallback-strategy", selected.candidate.id)
        assertFalse(selected.requiresApproval)
    }

    @Test
    fun refusesLowConfidenceAndUnknownFailures() {
        val selector = MechanismSelector()
        assertIs<MechanismSelection.NoSafeCandidate>(
            selector.select(evidence.copy(confidence = 0.4), emptyList())
        )
        assertIs<MechanismSelection.NoSafeCandidate>(
            selector.select(evidence.copy(stage = FailureStage.UNKNOWN), emptyList())
        )
    }

    @Test
    fun highRiskMechanismRequiresApproval() {
        val result = MechanismSelector().select(
            evidence,
            listOf(
                MechanismCandidate("architecture-rewrite", AdaptationLevel.ARCHITECTURE, setOf(FailureStage.PARSING), 3, 1.0, false)
            )
        )
        val selected = assertIs<MechanismSelection.Selected>(result)
        assertTrue(selected.requiresApproval)
    }

    @Test
    fun ignoresCandidatesForDifferentFailureStages() {
        val result = MechanismSelector().select(
            evidence,
            listOf(
                MechanismCandidate("network-fallback", AdaptationLevel.STRATEGY, setOf(FailureStage.NETWORK), 1, 1.0, true)
            )
        )
        assertIs<MechanismSelection.NoSafeCandidate>(result)
    }
}
