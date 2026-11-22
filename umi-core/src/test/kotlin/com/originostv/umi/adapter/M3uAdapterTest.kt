package com.originostv.umi.adapter

import com.originostv.umi.model.FailureCategory
import com.originostv.umi.model.TransportProtocol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class M3uAdapterTest {
    private val adapter = M3uAdapter()

    @Test
    fun parsesChannelMetadataAndHlsCandidate() {
        val input = """
            #EXTM3U
            #EXTINF:-1 tvg-id="news.one" tvg-logo="https://cdn.example/logo.png" group-title="News",News One
            https://stream.example/live/index.m3u8
        """.trimIndent()
        val resources = assertNotNull(adapter.parse("source-a", input).value)
        assertEquals(1, resources.size)
        assertEquals("News One", resources.first().displayName)
        assertEquals("News", resources.first().group)
        assertEquals(TransportProtocol.HLS, resources.first().candidates.single().protocol)
        assertEquals("news.one", resources.first().sourceItemId)
    }

    @Test
    fun rejectsJavascriptAndFileSchemes() {
        val input = """
            #EXTM3U
            #EXTINF:-1,Bad
            javascript:alert(1)
            #EXTINF:-1,Local
            file:///etc/passwd
        """.trimIndent()
        val result = adapter.parse("source-a", input)
        assertNotNull(result.value)
        assertTrue(result.value.isEmpty())
        assertTrue(result.diagnostics.any { it.category == FailureCategory.UNSUPPORTED_SCHEME })
    }

    @Test
    fun rejectsOversizedInputWithoutPartialResult() {
        val result = M3uAdapter(maxCharacters = 8).parse("source-a", "#EXTM3U\nhttps://example.com/a.m3u8")
        assertNull(result.value)
        assertEquals(FailureCategory.INPUT_INVALID, result.diagnostics.single().category)
    }

    @Test
    fun malformedUriDoesNotCrashParser() {
        val result = adapter.parse("source-a", "#EXTM3U\nnot a url")
        assertNotNull(result.value)
        assertTrue(result.value.isEmpty())
        assertEquals(FailureCategory.UNSUPPORTED_SCHEME, result.diagnostics.single().category)
    }
    @Test
    fun sameTvgIdProducesStableCrossSourceIdentity() {
        val first = adapter.parse(
            "source-a",
            """#EXTM3U
#EXTINF:-1 tvg-id="news.one",News One
https://a.example/live.m3u8"""
        ).value!!.single()
        val second = adapter.parse(
            "source-b",
            """#EXTM3U
#EXTINF:-1 tvg-id="news.one",News One HD
https://b.example/live.m3u8"""
        ).value!!.single()

        assertEquals(first.identity.resourceId, second.identity.resourceId)
        assertEquals("tvg-id", first.identity.namespace)
        assertEquals(first.identity.namespace, second.identity.namespace)
        assertTrue(first.candidates.single().candidateId != second.candidates.single().candidateId)
    }

}
