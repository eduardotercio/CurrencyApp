package util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getCurrentMoment(): Instant {
    return Clock.System.now()
}

fun getCurrentLocalDateTime(): LocalDateTime {
    return getCurrentMoment().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun parseToMillis(date: String): Long {
    return Instant.parse(date).toEpochMilliseconds()
}