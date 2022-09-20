package com.tbahlai.calendarview.uimodels

data class UiCalendarInfo constructor(
    val calendarMode: CalendarMode,
    val weekCount: Int
)

enum class CalendarMode {
    MONTH_MODE,
    WEEK_MODE,
}