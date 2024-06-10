package util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


fun getCurrentLocalDateTime(): LocalDateTime {
    return getCurrentMoment().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun getCurrentTimeInMillis(): Long {
    return getCurrentMoment().toEpochMilliseconds()
}

private fun getCurrentMoment(): Instant {
    return Clock.System.now()
}

fun parseToMillis(date: String): Long {
    return Instant.parse(date).toEpochMilliseconds()
}

fun toInstant(millis: Long): Instant {
    val dateTime = parseFromMillis(millis)
    return dateTime.toInstant(TimeZone.currentSystemDefault())
}

fun parseFromMillis(millis: Long): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(millis)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}