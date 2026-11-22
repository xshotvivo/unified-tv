package com.originostv.umi.catalog

import com.originostv.umi.model.ResourceIdentity
import com.originostv.umi.model.SourceResource

data class CatalogResource(
    val identity: ResourceIdentity,
    val displayName: String,
    val groups: Set<String>,
    val sourceItems: List<SourceResource>,
    val candidates: List<com.originostv.umi.model.PlaybackCandidate>
)

/**
 * Catalog contract. Implementations must replace a source snapshot atomically:
 * callers must never observe a partially imported source.
 */
interface CatalogRepository {
    fun replaceSourceSnapshot(sourceId: String, resources: List<SourceResource>)
    fun removeSource(sourceId: String)
    fun allResources(): List<CatalogResource>
    fun search(query: String): List<CatalogResource>
}

/**
 * Deterministic in-memory implementation used by core contract tests.
 * Android production storage will implement the same contract transactionally.
 */
class InMemoryCatalogRepository : CatalogRepository {
    private val sourceSnapshots = linkedMapOf<String, List<SourceResource>>()

    @Synchronized
    override fun replaceSourceSnapshot(sourceId: String, resources: List<SourceResource>) {
        require(sourceId.isNotBlank()) { "sourceId must not be blank" }
        require(resources.all { it.sourceId == sourceId }) {
            "A source snapshot may contain records from only its owning source"
        }
        require(resources.map { it.sourceItemId }.distinct().size == resources.size) {
            "Duplicate sourceItemId values are not allowed within one source snapshot"
        }
        sourceSnapshots[sourceId] = resources.toList()
    }

    @Synchronized
    override fun removeSource(sourceId: String) {
        sourceSnapshots.remove(sourceId)
    }

    @Synchronized
    override fun allResources(): List<CatalogResource> = aggregate()

    @Synchronized
    override fun search(query: String): List<CatalogResource> {
        val needle = normalize(query)
        if (needle.isEmpty()) return aggregate()
        return aggregate().filter { resource ->
            normalize(resource.displayName).contains(needle) ||
                resource.groups.any { normalize(it).contains(needle) } ||
                resource.sourceItems.any { normalize(it.sourceItemId).contains(needle) }
        }
    }

    private fun aggregate(): List<CatalogResource> {
        return sourceSnapshots.values
            .flatten()
            .groupBy { it.identity.resourceType to it.identity.resourceId }
            .values
            .map { items ->
                val ordered = items.sortedWith(compareBy<SourceResource> { it.sourceId }.thenBy { it.sourceItemId })
                val first = ordered.first()
                CatalogResource(
                    identity = first.identity,
                    displayName = ordered.firstOrNull { it.displayName.isNotBlank() }?.displayName.orEmpty(),
                    groups = ordered.mapNotNull { it.group?.takeIf(String::isNotBlank) }.toSortedSet(),
                    sourceItems = ordered,
                    candidates = ordered.flatMap { it.candidates }.distinctBy { it.candidateId }
                )
            }
            .sortedWith(compareBy<CatalogResource> { normalize(it.displayName) }.thenBy { it.identity.resourceId })
    }

    private fun normalize(value: String): String =
        value.trim().lowercase().replace(Regex("\\s+"), " ")
}
