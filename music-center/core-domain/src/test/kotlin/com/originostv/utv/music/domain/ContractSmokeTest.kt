package com.originostv.utv.music.domain

fun main() {
    val now = 1_800_000_000_000L
    val allowedSource = MusicSourceRef(
        providerId = "local-library",
        providerItemId = "file-001",
        kind = SourceKind.LOCAL_FILE,
        opaqueLocator = "local:asset-001",
        availability = AvailabilityStatus.AVAILABLE,
        rights = setOf(RightsGrant(RightsPermission.PLAYBACK, AuthorizationStatus.AUTHORIZED)),
    )
    check(allowedSource.isPlayable(nowEpochMillis = now))

    val unknownRightsSource = allowedSource.copy(rights = emptySet())
    check(!unknownRightsSource.isPlayable(nowEpochMillis = now))

    val unavailableSource = allowedSource.copy(availability = AvailabilityStatus.NOT_FOUND)
    check(!unavailableSource.isPlayable(nowEpochMillis = now))

    val expiringSource = allowedSource.copy(
        rights = setOf(RightsGrant(RightsPermission.PLAYBACK, AuthorizationStatus.AUTHORIZED, validUntilEpochMillis = now - 1)),
    )
    check(!expiringSource.isPlayable(nowEpochMillis = now))

    val territorySource = allowedSource.copy(
        rights = setOf(RightsGrant(RightsPermission.PLAYBACK, AuthorizationStatus.AUTHORIZED, territoryCodes = setOf("IT"))),
    )
    check(territorySource.isPlayable(territory = "IT", nowEpochMillis = now))
    check(!territorySource.isPlayable(territory = "SG", nowEpochMillis = now))

    val resource = MusicResource(
        id = MusicResourceId("utv:track:001"),
        identity = TrackIdentity(recordingId = "recording-a", releaseId = "release-a"),
        title = "Test Track",
        artistIds = listOf("artist-001"),
        sources = listOf(allowedSource),
        technical = AudioTechnicalMetadata(codec = "flac", sampleRateHz = 96_000, bitDepth = 24, channels = 2),
        updatedAtEpochMillis = now,
    )
    check(resource.title == "Test Track")
    check(resource.identity.recordingId == "recording-a")

    check(runCatching { MusicResourceId(" ") }.isFailure)
    check(runCatching {
        MusicResource(
            id = MusicResourceId("utv:track:bad"), title = " ", sources = listOf(allowedSource), updatedAtEpochMillis = now,
        )
    }.isFailure)
    check(runCatching {
        MusicResource(
            id = MusicResourceId("utv:track:duplicate"), title = "Duplicate", sources = listOf(allowedSource, allowedSource), updatedAtEpochMillis = now,
        )
    }.isFailure)

    println("PASS: MusicResource domain contract smoke tests")
}
