package com.originostv.aic

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RuntimeObservationTest {
    private fun metric(
        name: String,
        at: String,
        value: Double = 1.0,
        policy: String? = "policy-v1",
        canary: String? = "baseline"
    ) = RuntimeMetric(
        name = name,
        value = value,
        unit = MetricUnit.COUNT,
        domain = MetricDomain.APPLICATION,
        observedAt = Instant.parse(at),
        component = "umi-core",
        version = "0.1.0",
        policyVersion = policy,
        canaryGroup = canary
    )

    @Test
    fun `buffer remains bounded and evicts oldest sample`() {
        val store = InMemoryRuntimeObservationStore(capacity = 2)
        store.record(metric("a", "2026-01-01T00:00:00Z"))
        store.record(metric("b", "2026-01-01T00:00:01Z"))
        store.record(metric("c", "2026-01-01T00:00:02Z"))

        assertEquals(listOf("b", "c"), store.snapshot().map { it.name })
    }

    @Test
    fun `query uses inclusive time boundary and preserves policy metadata`() {
        val store = InMemoryRuntimeObservationStore(capacity = 10)
        store.record(metric("old", "2026-01-01T00:00:00Z"))
        store.record(metric("boundary", "2026-01-01T00:00:01Z", policy = "policy-v2", canary = "canary-5"))
        store.record(metric("new", "2026-01-01T00:00:02Z"))

        val result = store.querySince(Instant.parse("2026-01-01T00:00:01Z"))

        assertEquals(listOf("boundary", "new"), result.map { it.name })
        assertEquals("policy-v2", result.first().policyVersion)
        assertEquals("canary-5", result.first().canaryGroup)
    }

    @Test
    fun `query limit returns most recent matching samples`() {
        val store = InMemoryRuntimeObservationStore(capacity = 10)
        repeat(4) { index ->
            store.record(metric("m$index", "2026-01-01T00:00:0${index}Z"))
        }

        assertEquals(listOf("m2", "m3"), store.querySince(Instant.EPOCH, limit = 2).map { it.name })
    }

    @Test
    fun `invalid capacity and non finite metric values are rejected`() {
        assertFailsWith<IllegalArgumentException> { InMemoryRuntimeObservationStore(0) }
        assertFailsWith<IllegalArgumentException> {
            metric("bad", "2026-01-01T00:00:00Z", value = Double.NaN)
        }
    }
}
