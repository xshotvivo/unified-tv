package com.originostv.umi.adapter

import com.originostv.umi.model.*
import kotlinx.serialization.json.*
import java.net.URI
import java.security.MessageDigest

/**
 * Parses the TVBox top-level sites catalog shape only.
 * It deliberately does not execute spider/JAR/JS plugins or call provider APIs.
 */
class TvBoxJsonAdapter(
    private val maxCharacters: Int = 2_000_000,
    private val maxSites: Int = 5_000
) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = false }

    fun parse(sourceId: String, content: String): AdapterResult<List<SourceResource>> {
        if (content.length > maxCharacters) return failure(FailureCategory.INPUT_INVALID, "TVBox JSON exceeds configured character limit")
        val root = runCatching { json.parseToJsonElement(content).jsonObject }.getOrElse {
            return failure(FailureCategory.INPUT_INVALID, "TVBox JSON is not a valid JSON object")
        }
        val sites = root["sites"] as? JsonArray
            ?: return failure(FailureCategory.SCHEMA_UNSUPPORTED, "Supported schema requires a top-level sites array")
        if (sites.size > maxSites) return failure(FailureCategory.INPUT_INVALID, "TVBox JSON exceeds configured site limit")

        val resources = mutableListOf<SourceResource>()
        val diagnostics = mutableListOf<IntegrationDiagnostic>()
        sites.forEachIndexed { index, element ->
            val site = element as? JsonObject
            if (site == null) {
                diagnostics += IntegrationDiagnostic(FailureCategory.SOURCE_PARSE_FAILED, "Site entry ignored: expected JSON object", index)
                return@forEachIndexed
            }
            // The schema requires key/name/api to be JSON strings. JsonPrimitive.content
            // alone also accepts numbers and booleans, which can silently create bad IDs.
            val key = site.string("key")
            val name = site.string("name")
            val api = site.string("api")
            if (key.isNullOrBlank() || name.isNullOrBlank() || api.isNullOrBlank()) {
                diagnostics += IntegrationDiagnostic(FailureCategory.SOURCE_PARSE_FAILED, "Site entry ignored: key, name and api must be non-empty strings", index)
                return@forEachIndexed
            }
            val uri = runCatching { URI(api) }.getOrNull()
            if (uri == null || !(uri.scheme.equals("http", true) || uri.scheme.equals("https", true)) ||
                uri.host.isNullOrBlank() || uri.userInfo != null) {
                diagnostics += IntegrationDiagnostic(FailureCategory.UNSUPPORTED_SCHEME, "Site API must be an absolute HTTP(S) URL without embedded user info", index)
                return@forEachIndexed
            }
            val resourceId = "provider:" + stableId("$sourceId|$key")
            resources += SourceResource(
                sourceId = sourceId,
                sourceItemId = key,
                identity = ResourceIdentity(resourceId, ResourceType.PROVIDER, "tvbox:$sourceId"),
                displayName = name,
                // TVBox's type is commonly numeric; accept a JSON scalar without
                // weakening validation of the required string fields above.
                group = site.scalarText("type")
            )
        }
        return AdapterResult(resources, diagnostics)
    }

    private fun JsonObject.string(key: String): String? =
        (this[key] as? JsonPrimitive)
            ?.takeIf { it.isString }
            ?.content
            ?.takeIf(String::isNotBlank)

    private fun JsonObject.scalarText(key: String): String? =
        (this[key] as? JsonPrimitive)
            ?.takeUnless { it is JsonNull }
            ?.content
            ?.takeIf(String::isNotBlank)

    private fun stableId(value: String): String =
        MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8))
            .take(12).joinToString("") { "%02x".format(it) }

    private fun failure(category: FailureCategory, message: String) =
        AdapterResult<List<SourceResource>>(null, listOf(IntegrationDiagnostic(category, message)))
}
