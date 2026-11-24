package com.originostv.aic

import java.time.Instant

/** Stable, backend-neutral metric dimensions consumed by AIC. */
enum class MetricDomain { SYSTEM, APPLICATION, BUSINESS }

enum class MetricUnit {
    COUNT, BYTES, MILLISECONDS, SECONDS, PERCENT, OPERATIONS_PER_SECOND, RATIO, NONE
}

/**
 * A single immutable observation.
 *
 * Keep labels bounded and low-cardinality. User IDs, URLs and arbitrary exception
 * messages belong in logs/traces, not metric tags.
 */
data class RuntimeMetric(
    val name: String,
    val value: Double,
    val unit: MetricUnit,
    val domain: MetricDomain,
    val observedAt: Instant,
    val component: String,
    val version: String? = null,
    val policyVersion: String? = null,
    val canaryGroup: String? = null,
    val traceId: String? = null,
    val tags: Map<String, String> = emptyMap()
) {
    init {
        require(name.isNotBlank()) { "Metric name must not be blank" }
        require(component.isNotBlank()) { "Component must not be blank" }
        require(value.isFinite()) { "Metric value must be finite" }
        require(tags.size <= MAX_TAGS) { "Too many metric tags; use logs or traces for high-cardinality data" }
        require(tags.keys.all { it.isNotBlank() }) { "Metric tag keys must not be blank" }
    }

    private companion object {
        const val MAX_TAGS = 12
    }
}

/** Read-only observation contract. Implementations must return stable snapshots. */
interface RuntimeObservationStore {
    fun record(metric: RuntimeMetric)
    fun querySince(sinceInclusive: Instant, limit: Int = DEFAULT_QUERY_LIMIT): List<RuntimeMetric>
    fun snapshot(): List<RuntimeMetric>

    companion object {
        const val DEFAULT_QUERY_LIMIT = 10_000
    }
}

/**
 * Bounded in-memory ring buffer for tests and single-process deployments.
 * Production exporters can implement RuntimeObservationStore without changing AIC.
 */
class InMemoryRuntimeObservationStore(
    private val capacity: Int = DEFAULT_CAPACITY
) : RuntimeObservationStore {
    private val samples = ArrayDeque<RuntimeMetric>()

    init {
        require(capacity > 0) { "Capacity must be greater than zero" }
    }

    @Synchronized
    override fun record(metric: RuntimeMetric) {
        if (samples.size == capacity) samples.removeFirst()
        samples.addLast(metric)
    }

    @Synchronized
    override fun querySince(sinceInclusive: Instant, limit: Int): List<RuntimeMetric> {
        require(limit >= 0) { "Limit must not be negative" }
        if (limit == 0) return emptyList()
        return samples.filter { !it.observedAt.isBefore(sinceInclusive) }.takeLast(limit)
    }

    @Synchronized
    override fun snapshot(): List<RuntimeMetric> = samples.toList()

    companion object {
        const val DEFAULT_CAPACITY = 20_000
    }
}
