package com.originostv.aic

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AicSafetyGatewayTest {
    private val policy = SafetyPolicyTable(
        automaticAllow = mapOf("L1" to listOf("LOW")),
        confirmationRequired = mapOf("L2" to listOf("LOW", "MEDIUM", "HIGH"))
    )
    private val gateway = AicSafetyGateway(policy)
    private val request = AicActionRequest("req-1", "fallback-vlc", "player.engine", "Switch playback engine")

    private fun input(
        tier: AicPermissionTier = AicPermissionTier.L1,
        risk: AicRiskLevel = AicRiskLevel.LOW,
        registered: Boolean = true,
        capability: CapabilityMatrixRow? = CapabilityMatrixRow("fallback-vlc", true, AicRiskLevel.HIGH),
        budget: AicBudgetState = AicBudgetState(2),
        emergencyStop: Boolean = false,
        cooldown: Boolean = false
    ) = SafetyGatewayInput(request, tier, risk, registered, capability, budget, emergencyStop, cooldown)

    @Test fun permitsOnlyRegisteredSupportedLowRiskL1Action() {
        assertEquals(GatewayDecision.ALLOW, gateway.evaluate(input()))
    }

    @Test fun l2RequiresExplicitUserConfirmation() {
        assertEquals(GatewayDecision.REQUIRE_USER_CONFIRMATION, gateway.evaluate(input(tier = AicPermissionTier.L2)))
    }

    @Test fun l3NeverReceivesWriteAuthorization() {
        listOf(AicRiskLevel.LOW, AicRiskLevel.MEDIUM, AicRiskLevel.HIGH, AicRiskLevel.CRITICAL).forEach { risk ->
            val result = gateway.evaluate(input(tier = AicPermissionTier.L3, risk = risk))
            assertTrue(result == GatewayDecision.ALERT_ONLY || result == GatewayDecision.DENY)
            assertFalse(result == GatewayDecision.ALLOW || result == GatewayDecision.REQUIRE_USER_CONFIRMATION)
        }
    }

    @Test fun emergencyStopRejectsEveryAction() {
        assertEquals(GatewayDecision.DENY, gateway.evaluate(input(emergencyStop = true)))
    }

    @Test fun unregisteredMechanismIsRejected() {
        assertEquals(GatewayDecision.DENY, gateway.evaluate(input(registered = false)))
    }

    @Test fun missingCapabilityRowIsRejected() {
        assertEquals(GatewayDecision.DENY, gateway.evaluate(input(capability = null)))
    }

    @Test fun unsupportedOrMismatchedCapabilityIsRejected() {
        assertEquals(GatewayDecision.DENY, gateway.evaluate(input(
            capability = CapabilityMatrixRow("fallback-vlc", false, AicRiskLevel.HIGH))))
        assertEquals(GatewayDecision.DENY, gateway.evaluate(input(
            capability = CapabilityMatrixRow("other-mechanism", true, AicRiskLevel.HIGH))))
    }

    @Test fun riskAboveCapabilityMaximumIsRejected() {
        assertEquals(GatewayDecision.DENY, gateway.evaluate(input(
            risk = AicRiskLevel.CRITICAL,
            capability = CapabilityMatrixRow("fallback-vlc", true, AicRiskLevel.HIGH))))
    }

    @Test fun exhaustedBudgetAndCooldownCannotAuthorizeWrites() {
        assertEquals(GatewayDecision.ALERT_ONLY, gateway.evaluate(input(budget = AicBudgetState(0))))
        assertEquals(GatewayDecision.ALERT_ONLY, gateway.evaluate(input(cooldown = true)))
    }

    @Test fun l0CannotWriteAndUnlistedRiskIsRejected() {
        assertEquals(GatewayDecision.ALERT_ONLY, gateway.evaluate(input(tier = AicPermissionTier.L0)))
        assertEquals(GatewayDecision.DENY, gateway.evaluate(input(risk = AicRiskLevel.MEDIUM)))
    }

    @Test fun bundledPolicyIsDataDriven() {
        val loaded = SafetyPolicyTable.bundled()
        assertEquals(listOf("LOW"), loaded.automaticAllow["L1"])
        assertEquals(listOf("LOW", "MEDIUM", "HIGH"), loaded.confirmationRequired["L2"])
    }

    @Test fun auditHashChainCoversDecisionsAndRollback() {
        val audit = AicHashChainAuditLog()
        val now = Instant.parse("2026-11-25T00:00:00Z")
        audit.appendGatewayDecision(input(), GatewayDecision.ALLOW, now)
        audit.appendRollback("journal-1", "player.engine", "RESTORED", now.plusSeconds(1))
        assertEquals(2, audit.snapshot().size)
        assertEquals(AicAuditEventType.ROLLBACK, audit.snapshot().last().eventType)
        assertTrue(audit.verify())
    }
    @Test fun auditedEntryPointRecordsEveryDecisionAndRollback() {
        val audit = AicHashChainAuditLog()
        val service = AicAuditedSafetyGateway(policy, audit) { Instant.parse("2026-11-25T00:00:00Z") }
        assertEquals(GatewayDecision.ALLOW, service.decide(input()))
        assertEquals(GatewayDecision.DENY, service.decide(input(emergencyStop = true)))
        service.recordRollback("journal-2", "player.engine", "RESTORED")
        assertEquals(3, audit.snapshot().size)
        assertTrue(audit.verify())
    }

}
