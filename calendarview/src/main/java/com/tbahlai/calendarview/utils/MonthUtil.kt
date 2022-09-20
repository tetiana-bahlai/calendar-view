package com.tbahlai.calendarview.utils

import java.time.YearMonth

fun YearMonth.previous(): YearMonth {
    return this.minusMonths(1)
}

fun YearMonth.next(): YearMonth {
    return this.plusMonths(1)
}