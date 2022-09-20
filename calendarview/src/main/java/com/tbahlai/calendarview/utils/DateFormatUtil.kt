package com.tbahlai.calendarview.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun LocalDate.toInstant(): Instant {
    return this.atStartOfDay().toInstant(ZoneOffset.UTC)
}

fun Instant.daysBetween(date: Instant) : Long {
    val millisBetween = abs(this.toEpochMilli() - date.toEpochMilli())
    return TimeUnit.MILLISECONDS.toDays(millisBetween)
}