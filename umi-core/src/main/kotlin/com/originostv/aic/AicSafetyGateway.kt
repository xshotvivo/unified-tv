package com.originostv.aic

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Instant

enum class AicPermissionTier { L0, L1, L2, L3 }
enum class AicRiskLevel { LOW, MEDIUM, HIGH, CRITICAL }
enum class GatewayDecision { ALLOW, REQUIRE_USER_CONFIRMATION, ALERT_ONLY, DENY }

data class AicActionRequest(
    val requestId: String,
    val mechanismId: String,
    val resourceKey: String,
    val description: String
) {
    init {
        require(requestId.isNotBlank())
        require(mechanismId.isNotBlank())
        require(resourceKey.isNotBlank())
        require(description.isNotBlank())
    }
}

data class CapabilityMatrixRow(
    val mechanismId: String,
    val supported: Boolean,
    val maximumRisk: AicRiskLevel
)

data class AicBudgetState(val remainingActions: Int) {
    init { require(remainingActions >= 0) }
    val exhausted: Boolean get() = remainingActions == 0
}

@Serializable
data class SafetyPolicyTable(
    val automaticAllow: Map<String, List<String>>,
    val confirmationRequired: Map<String, List<String>>
) {
    companion object {
        fun fromJson(json: String): SafetyPolicyTable =
            Json { ignoreUnknownKeys = false }.decodeFromString(json)

        fun bundled(): SafetyPolicyTable {
            val stream = SafetyPolicyTable::class.java.getResourceAsStream("/aic/safety-policy.json")
                ?: error("Missing /aic/safety-policy.json")
            return stream.bufferedReader(StandardCharsets.UTF_8).use { fromJson(it.readText()) }
        }
    }
}

/** All inputs are explicit; this evaluator has no I/O, clock, model, or mutation side effects. */
data class SafetyGatewayInput(
    val request: AicActionRequest,
    val permissionTier: AicPermissionTier,
    val riskLevel: AicRiskLevel,
    val mechanismRegistered: Boolean,
    val capabilityRow: CapabilityMatrixRow?,
    val budget: AicBudgetState,
    val emergencyStopActive: Boolean,
    val cooldownActive: Boolean
)

internal class AicSafetyGateway(private val policy: SafetyPolicyTable) {
    internal fun evaluate(input: SafetyGatewayInput): GatewayDecision {
        if (input.emergencyStopActive) return GatewayDecision.DENY
        if (!input.mechanismRegistered) return GatewayDecision.DENY
        val capability = input.capabilityRow ?: return GatewayDecision.DENY
        if (!capability.supported || capability.mechanismId != input.request.mechanismId) {
            return GatewayDecision.DENY
        }
        if (input.riskLevel.ordinal > capability.maximumRisk.ordinal) return GatewayDecision.DENY
        if (input.cooldownActive || input.budget.exhausted) return GatewayDecision.ALERT_ONLY

        // L3 is deliberately terminal: no policy-table entry can grant it write authority.
        if (input.permissionTier == AicPermissionTier.L3) return GatewayDecision.ALERT_ONLY
        if (input.permissionTier == AicPermissionTier.L0) return GatewayDecision.ALERT_ONLY

        val tier = input.permissionTier.name
        val risk = input.riskLevel.name
        val automatic = policy.automaticAllow[tier].orEmpty().contains(risk)
        if (automatic) return GatewayDecision.ALLOW

        val confirmation = policy.confirmationRequired[tier].orEmpty().contains(risk)
        return if (confirmation) GatewayDecision.REQUIRE_USER_CONFIRMATION else GatewayDecision.DENY
    }
}

enum class AicAuditEventType { GATEWAY_DECISION, ROLLBACK }

data class AicAuditRecord(
    val sequence: Long,
    val occurredAt: Instant,
    val eventType: AicAuditEventType,
    val subject: String,
    val outcome: String,
    val previousHash: String,
    val hash: String
)

/**
 * Append-only, in-process hash-chain reference implementation.
 * Durable storage, key protection, and tamper resistance across process/device boundaries
 * require a separately verified platform implementation.
 */
class AicHashChainAuditLog {
    private val records = mutableListOf<AicAuditRecord>()

    @Synchronized
    fun appendGatewayDecision(input: SafetyGatewayInput, decision: GatewayDecision, at: Instant): AicAuditRecord =
        append(
            at,
            AicAuditEventType.GATEWAY_DECISION,
            listOf(
                input.request.requestId,
                input.request.mechanismId,
                input.request.resourceKey,
                input.permissionTier.name,
                input.riskLevel.name,
                input.mechanismRegistered.toString(),
                input.capabilityRow?.let { "${it.mechanismId}:${it.supported}:${it.maximumRisk}" } ?: "null",
                input.budget.remainingActions.toString(),
                input.emergencyStopActive.toString(),
                input.cooldownActive.toString()
            ).joinToString("|"),
            decision.name
        )

    /** Rollback bypasses positive-write authorization but never bypasses audit. */
    @Synchronized
    fun appendRollback(journalId: String, resourceKey: String, outcome: String, at: Instant): AicAuditRecord {
        require(journalId.isNotBlank() && resourceKey.isNotBlank() && outcome.isNotBlank())
        return append(at, AicAuditEventType.ROLLBACK, "$journalId|$resourceKey", outcome)
    }

    @Synchronized
    fun snapshot(): List<AicAuditRecord> = records.toList()

    @Synchronized
    fun verify(): Boolean {
        var previous = GENESIS_HASH
        records.forEachIndexed { index, record ->
            if (record.sequence != index.toLong() || record.previousHash != previous) return false
            val expected = digest(canonical(record.sequence, record.occurredAt, record.eventType,
                record.subject, record.outcome, record.previousHash))
            if (record.hash != expected) return false
            previous = record.hash
        }
        return true
    }

    private fun append(at: Instant, type: AicAuditEventType, subject: String, outcome: String): AicAuditRecord {
        val previous = records.lastOrNull()?.hash ?: GENESIS_HASH
        val sequence = records.size.toLong()
        val hash = digest(canonical(sequence, at, type, subject, outcome, previous))
        return AicAuditRecord(sequence, at, type, subject, outcome, previous, hash).also(records::add)
    }

    private fun canonical(
        sequence: Long, at: Instant, type: AicAuditEventType,
        subject: String, outcome: String, previous: String
    ): String = listOf(sequence.toString(), at.toString(), type.name, subject, outcome, previous)
        .joinToString("") { "${it.length}:$it" }

    private fun digest(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray(StandardCharsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

    private companion object { const val GENESIS_HASH = "0".repeat(64) }
}


/**
 * Mandatory orchestration boundary for positive actions: evaluate first, then append
 * the decision before returning it to any executor. Executors must consume this result,
 * never call the pure evaluator directly.
 */
class AicAuditedSafetyGateway(
    policy: SafetyPolicyTable,
    private val auditLog: AicHashChainAuditLog,
    private val clock: () -> Instant
) {
    private val gateway = AicSafetyGateway(policy)
    fun decide(input: SafetyGatewayInput): GatewayDecision {
        val decision = gateway.evaluate(input)
        auditLog.appendGatewayDecision(input, decision, clock())
        return decision
    }

    /** Rollback bypasses positive-write authorization, but its outcome is still audited. */
    fun recordRollback(journalId: String, resourceKey: String, outcome: String) {
        auditLog.appendRollback(journalId, resourceKey, outcome, clock())
    }
}
