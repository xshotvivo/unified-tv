package com.originostv.umi.model

import kotlinx.serialization.Serializable

@Serializable enum class SourceFamily { M3U, TXT, TVBOX_JSON }
@Serializable enum class ResourceType { CHANNEL, PROVIDER }
@Serializable enum class TransportProtocol { HTTP, HTTPS, HLS, UNKNOWN }
@Serializable enum class SourceHealthState { CONFIGURED, PARSED, REACHABLE, MEDIA_VALID, FIRST_FRAME, SUSTAINED, DEGRADED, UNAVAILABLE, UNKNOWN }
@Serializable enum class FailureCategory { INPUT_INVALID, SCHEMA_UNSUPPORTED, SOURCE_FETCH_FAILED, SOURCE_PARSE_FAILED, UNSUPPORTED_SCHEME, SECURITY_POLICY_BLOCKED, UNKNOWN }

@Serializable
data class SourceDescriptor(
    val sourceId: String,
    val family: SourceFamily,
    val label: String,
    val adapterId: String,
    val adapterVersion: Int = 1,
    val origin: String? = null,
    val enabled: Boolean = true
)

@Serializable
data class ResourceIdentity(val resourceId: String, val resourceType: ResourceType, val namespace: String)

@Serializable
data class PlaybackCandidate(
    val candidateId: String,
    val resourceId: String,
    val sourceId: String,
    val locator: String,
    val protocol: TransportProtocol,
    val health: SourceHealthState = SourceHealthState.UNKNOWN
)

@Serializable
data class SourceResource(
    val sourceId: String,
    val sourceItemId: String,
    val identity: ResourceIdentity,
    val displayName: String,
    val group: String? = null,
    val logoUri: String? = null,
    val candidates: List<PlaybackCandidate> = emptyList()
)

@Serializable
data class IntegrationDiagnostic(
    val category: FailureCategory,
    val message: String,
    val itemIndex: Int? = null
)

data class AdapterResult<T>(val value: T?, val diagnostics: List<IntegrationDiagnostic> = emptyList()) {
    val isSuccess: Boolean
        get() = value != null && diagnostics.none {
            it.category == FailureCategory.INPUT_INVALID || it.category == FailureCategory.SCHEMA_UNSUPPORTED
        }
}
