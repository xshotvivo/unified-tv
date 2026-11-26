package com.originostv.utv.music.domain

@JvmInline
value class MusicResourceId(val value: String) {
    init { require(value.isNotBlank()) { "MusicResourceId must not be blank" } }
}

data class TrackIdentity(
    val recordingId: String? = null,
    val releaseId: String? = null,
    val editionId: String? = null,
) {
    init {
        require(listOfNotNull(recordingId, releaseId, editionId).all { it.isNotBlank() }) {
            "Track identity values must not be blank when present"
        }
    }
}

enum class SourceKind { LOCAL_FILE, SELF_HOSTED_SERVER, AUTHORIZED_PROVIDER }
enum class AvailabilityStatus { AVAILABLE, TEMPORARILY_UNAVAILABLE, NOT_FOUND, AUTH_REQUIRED, UNKNOWN }
enum class RightsPermission { PLAYBACK, DOWNLOAD, KARAOKE, REDISTRIBUTION }
enum class AuthorizationStatus { AUTHORIZED, PROHIBITED, UNKNOWN }
enum class MusicAssetKind { AUDIO, LYRICS, ARTWORK, INSTRUMENTAL, VOCAL, MUSIC_VIDEO }

data class RightsGrant(
    val permission: RightsPermission,
    val status: AuthorizationStatus,
    val territoryCodes: Set<String> = emptySet(),
    val validUntilEpochMillis: Long? = null,
    val evidenceRef: String? = null,
) {
    init {
        require(territoryCodes.none { it.isBlank() }) { "Territory codes must not be blank" }
        require(validUntilEpochMillis == null || validUntilEpochMillis >= 0) { "Invalid rights expiry" }
    }

    fun appliesTo(territory: String?, nowEpochMillis: Long): Boolean {
        val territoryMatches = territoryCodes.isEmpty() || (territory != null && territory in territoryCodes)
        val notExpired = validUntilEpochMillis == null || nowEpochMillis < validUntilEpochMillis
        return territoryMatches && notExpired
    }
}

data class AudioTechnicalMetadata(
    val codec: String? = null,
    val sampleRateHz: Int? = null,
    val bitDepth: Int? = null,
    val channels: Int? = null,
    val durationMillis: Long? = null,
) {
    init {
        require(codec == null || codec.isNotBlank()) { "Codec must not be blank" }
        require(sampleRateHz == null || sampleRateHz > 0) { "Sample rate must be positive" }
        require(bitDepth == null || bitDepth > 0) { "Bit depth must be positive" }
        require(channels == null || channels > 0) { "Channel count must be positive" }
        require(durationMillis == null || durationMillis > 0) { "Duration must be positive" }
    }
}

data class MusicAssetRef(
    val assetId: String,
    val kind: MusicAssetKind,
    val providerId: String,
    val opaqueLocator: String,
    val mimeType: String? = null,
) {
    init {
        require(assetId.isNotBlank())
        require(providerId.isNotBlank())
        require(opaqueLocator.isNotBlank())
        require(mimeType == null || mimeType.isNotBlank())
    }
}

data class MusicSourceRef(
    val providerId: String,
    val providerItemId: String,
    val kind: SourceKind,
    val opaqueLocator: String,
    val availability: AvailabilityStatus = AvailabilityStatus.UNKNOWN,
    val rights: Set<RightsGrant> = emptySet(),
    val checkedAtEpochMillis: Long? = null,
) {
    init {
        require(providerId.isNotBlank())
        require(providerItemId.isNotBlank())
        require(opaqueLocator.isNotBlank())
        require(checkedAtEpochMillis == null || checkedAtEpochMillis >= 0)
    }

    fun authorizationFor(
        permission: RightsPermission,
        territory: String? = null,
        nowEpochMillis: Long,
    ): AuthorizationStatus {
        val applicable = rights.filter { it.permission == permission && it.appliesTo(territory, nowEpochMillis) }
        return when {
            applicable.any { it.status == AuthorizationStatus.AUTHORIZED } -> AuthorizationStatus.AUTHORIZED
            applicable.any { it.status == AuthorizationStatus.PROHIBITED } -> AuthorizationStatus.PROHIBITED
            else -> AuthorizationStatus.UNKNOWN
        }
    }

    fun isPlayable(territory: String? = null, nowEpochMillis: Long): Boolean =
        availability == AvailabilityStatus.AVAILABLE &&
            authorizationFor(RightsPermission.PLAYBACK, territory, nowEpochMillis) == AuthorizationStatus.AUTHORIZED
}

data class MusicResource(
    val id: MusicResourceId,
    val identity: TrackIdentity = TrackIdentity(),
    val title: String,
    val artistIds: List<String> = emptyList(),
    val albumId: String? = null,
    val discNumber: Int? = null,
    val trackNumber: Int? = null,
    val sources: List<MusicSourceRef>,
    val assets: List<MusicAssetRef> = emptyList(),
    val technical: AudioTechnicalMetadata? = null,
    val updatedAtEpochMillis: Long,
) {
    init {
        require(title.isNotBlank()) { "Track title must not be blank" }
        require(artistIds.none { it.isBlank() }) { "Artist IDs must not be blank" }
        require(albumId == null || albumId.isNotBlank()) { "Album ID must not be blank" }
        require(discNumber == null || discNumber > 0) { "Disc number must be positive" }
        require(trackNumber == null || trackNumber > 0) { "Track number must be positive" }
        require(sources.isNotEmpty()) { "A music resource must have at least one source" }
        require(sources.map { it.providerId to it.providerItemId }.distinct().size == sources.size) {
            "Duplicate provider source reference"
        }
        require(assets.map { it.assetId }.distinct().size == assets.size) { "Duplicate asset ID" }
        require(updatedAtEpochMillis >= 0) { "Updated timestamp must not be negative" }
    }
}
