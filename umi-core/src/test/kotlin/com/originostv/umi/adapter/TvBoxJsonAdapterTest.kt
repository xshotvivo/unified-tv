package com.originostv.umi.adapter

import com.originostv.umi.model.FailureCategory
import com.originostv.umi.model.ResourceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TvBoxJsonAdapterTest {
    private val adapter = TvBoxJsonAdapter()

    @Test
    fun parsesSupportedSitesCatalog() {
        val input = """
            {
              "sites": [
                {"key": "alpha", "name": "Alpha", "api": "https://api.example/v1", "type": 0},
                {"key": "beta", "name": "Beta", "api": "https://beta.example/api"}
              ],
              "spider": "not-executed"
            }
        """.trimIndent()
        val resources = assertNotNull(adapter.parse("tvbox-source", input).value)
        assertEquals(2, resources.size)
        assertEquals(ResourceType.PROVIDER, resources.first().identity.resourceType)
        assertEquals("Alpha", resources.first().displayName)
        // TVBox commonly represents the provider type as a number; preserve it as text.
        assertEquals("0", resources.first().group)
    }

    @Test
    fun rejectsUnknownTopLevelSchema() {
        val result = adapter.parse("tvbox-source", """{"providers": []}""")
        assertNull(result.value)
        assertEquals(FailureCategory.SCHEMA_UNSUPPORTED, result.diagnostics.single().category)
    }

    @Test
    fun ignoresInvalidSiteAndKeepsValidSites() {
        val result = adapter.parse(
            "tvbox-source",
            """{"sites":[{"key":"bad","name":"Bad","api":"file:///tmp/x"},{"key":"ok","name":"OK","api":"https://api.example"}]}"""
        )
        val resources = assertNotNull(result.value)
        assertEquals(1, resources.size)
        assertEquals("ok", resources.single().sourceItemId)
        assertEquals(FailureCategory.UNSUPPORTED_SCHEME, result.diagnostics.single().category)
    }

    @Test
    fun rejectsNonStringRequiredFields() {
        val invalidSites = listOf(
            """{"key":7,"name":"Numeric key","api":"https://api.example"}""",
            """{"key":"numeric-name","name":42,"api":"https://api.example"}""",
            """{"key":"numeric-api","name":"Numeric API","api":123}"""
        )

        invalidSites.forEach { site ->
            val result = adapter.parse("tvbox-source", """{"sites":[$site]}""")
            assertTrue(assertNotNull(result.value).isEmpty(), "Expected invalid site to be ignored: $site")
            assertEquals(FailureCategory.SOURCE_PARSE_FAILED, result.diagnostics.single().category)
        }
    }
}
