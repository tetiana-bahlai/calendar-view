package com.tbahlai.calendarview.utils

import java.time.Year

fun getYears(from: Long, to: Long) : List<Year> {
    val currentYear = Year.now()
    val firstYear = currentYear.minusYears(from)
    val lastYear = currentYear.plusYears(to)
    val listOfYears = mutableListOf<Year>()
    for (year in firstYear.value..lastYear.value) {
        listOfYears.add(Year.of(year))
    }
    return listOfYears
}