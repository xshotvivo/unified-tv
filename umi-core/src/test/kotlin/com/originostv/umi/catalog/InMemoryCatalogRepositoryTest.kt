package com.originostv.umi.catalog

import com.originostv.umi.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class InMemoryCatalogRepositoryTest {
    private fun channel(sourceId: String, name: String, locator: String, itemId: String = "news.id") =
        SourceResource(
            sourceId = sourceId,
            sourceItemId = itemId,
            identity = ResourceIdentity("channel:news-id", ResourceType.CHANNEL, "tvg-id"),
            displayName = name,
            group = "News",
            candidates = listOf(
                PlaybackCandidate(
                    candidateId = "candidate:$sourceId",
                    resourceId = "channel:news-id",
                    sourceId = sourceId,
                    locator = locator,
                    protocol = TransportProtocol.HLS,
                    health = SourceHealthState.CONFIGURED
                )
            )
        )

    @Test
    fun mergesSameLogicalResourceAndPreservesBothCandidates() {
        val catalog = InMemoryCatalogRepository()
        catalog.replaceSourceSnapshot("source-a", listOf(channel("source-a", "News One", "https://a.example/live.m3u8")))
        catalog.replaceSourceSnapshot("source-b", listOf(channel("source-b", "News One HD", "https://b.example/live.m3u8")))

        val result = catalog.allResources()
        assertEquals(1, result.size)
        assertEquals(2, result.single().sourceItems.size)
        assertEquals(setOf("source-a", "source-b"), result.single().candidates.map { it.sourceId }.toSet())
    }

    @Test
    fun replacingSnapshotRemovesStaleItemsButKeepsOtherSources() {
        val catalog = InMemoryCatalogRepository()
        catalog.replaceSourceSnapshot("source-a", listOf(channel("source-a", "News One", "https://a.example/old.m3u8")))
        catalog.replaceSourceSnapshot("source-b", listOf(channel("source-b", "News One", "https://b.example/live.m3u8")))

        catalog.replaceSourceSnapshot("source-a", emptyList())

        val result = catalog.allResources()
        assertEquals(1, result.size)
        assertEquals(listOf("source-b"), result.single().candidates.map { it.sourceId })
    }

    @Test
    fun rejectsMixedSourceSnapshotWithoutChangingPreviousState() {
        val catalog = InMemoryCatalogRepository()
        catalog.replaceSourceSnapshot("source-a", listOf(channel("source-a", "News One", "https://a.example/live.m3u8")))

        assertFailsWith<IllegalArgumentException> {
            catalog.replaceSourceSnapshot("source-a", listOf(channel("source-b", "Injected", "https://b.example/live.m3u8")))
        }

        assertEquals(listOf("source-a"), catalog.allResources().single().candidates.map { it.sourceId })
    }

    @Test
    fun searchIsCaseInsensitiveAndMatchesGroup() {
        val catalog = InMemoryCatalogRepository()
        catalog.replaceSourceSnapshot("source-a", listOf(channel("source-a", "News One", "https://a.example/live.m3u8")))

        assertEquals(1, catalog.search("NEWS").size)
        assertEquals(1, catalog.search("news").size)
        assertTrue(catalog.search("sports").isEmpty())
    }
}
