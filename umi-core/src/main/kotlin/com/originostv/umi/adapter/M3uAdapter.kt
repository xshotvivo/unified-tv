package com.originostv.umi.adapter

import com.originostv.umi.model.*
import java.net.URI
import java.security.MessageDigest

/** First-pass parser. Only absolute HTTP(S) stream locators are accepted. */
class M3uAdapter(
    private val maxCharacters: Int = 5_000_000,
    private val maxEntries: Int = 50_000
) {
    fun parse(sourceId: String, content: String): AdapterResult<List<SourceResource>> {
        if (content.length > maxCharacters) return failure(FailureCategory.INPUT_INVALID, "Playlist exceeds configured character limit")

        val resources = mutableListOf<SourceResource>()
        val diagnostics = mutableListOf<IntegrationDiagnostic>()
        var pendingName: String? = null
        var pendingGroup: String? = null
        var pendingLogo: String? = null
        var pendingTvgId: String? = null

        for ((index, raw) in content.lineSequence().withIndex()) {
            val line = raw.trim()
            if (line.isBlank() || line == "#EXTM3U") continue
            if (line.startsWith("#EXTINF:", ignoreCase = true)) {
                val comma = line.indexOf(',')
                val attributes = if (comma >= 0) line.substring(0, comma) else line
                pendingName = if (comma >= 0) line.substring(comma + 1).trim().ifBlank { null } else null
                pendingGroup = attribute(attributes, "group-title")
                pendingLogo = attribute(attributes, "tvg-logo")
                pendingTvgId = attribute(attributes, "tvg-id")
                continue
            }
            if (line.startsWith("#")) continue

            val uri = parseHttpUri(line)
            if (uri == null) {
                diagnostics += IntegrationDiagnostic(FailureCategory.UNSUPPORTED_SCHEME, "Entry ignored: only absolute HTTP(S) URLs are accepted", index + 1)
                pendingName = null; pendingGroup = null; pendingLogo = null; pendingTvgId = null
                continue
            }
            if (resources.size >= maxEntries) return failure(FailureCategory.INPUT_INVALID, "Playlist exceeds configured entry limit")

            val name = pendingName ?: pendingTvgId ?: uri.host
            val itemId = pendingTvgId ?: stableHash("$sourceId|$line")
            val identityNamespace = if (pendingTvgId.isNullOrBlank()) "m3u:$sourceId" else "tvg-id"
            val resourceId = "channel:" + stableHash(itemId.lowercase())
            val protocol = if (uri.path.endsWith(".m3u8", true)) TransportProtocol.HLS
                else if (uri.scheme.equals("https", true)) TransportProtocol.HTTPS else TransportProtocol.HTTP
            val candidate = PlaybackCandidate(
                candidateId = "candidate:" + stableHash("$sourceId|$line"),
                resourceId = resourceId,
                sourceId = sourceId,
                locator = uri.toASCIIString(),
                protocol = protocol,
                health = SourceHealthState.CONFIGURED
            )
            resources += SourceResource(
                sourceId = sourceId,
                sourceItemId = itemId,
                identity = ResourceIdentity(resourceId, ResourceType.CHANNEL, identityNamespace),
                displayName = name,
                group = pendingGroup,
                logoUri = pendingLogo?.takeIf { parseHttpUri(it) != null },
                candidates = listOf(candidate)
            )
            pendingName = null; pendingGroup = null; pendingLogo = null; pendingTvgId = null
        }
        return AdapterResult(resources, diagnostics)
    }

    private fun attribute(line: String, name: String): String? {
        val regex = Regex("""(?:^|\s)$name="([^"]*)"""", RegexOption.IGNORE_CASE)
        return regex.find(line)?.groupValues?.getOrNull(1)?.takeIf(String::isNotBlank)
    }

    private fun parseHttpUri(value: String): URI? = runCatching {
        URI(value).takeIf {
            (it.scheme.equals("http", true) || it.scheme.equals("https", true)) &&
                !it.host.isNullOrBlank() && it.userInfo == null
        }
    }.getOrNull()

    private fun stableHash(value: String): String =
        MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8))
            .take(12).joinToString("") { "%02x".format(it) }

    private fun failure(category: FailureCategory, message: String) =
        AdapterResult<List<SourceResource>>(null, listOf(IntegrationDiagnostic(category, message)))
}
