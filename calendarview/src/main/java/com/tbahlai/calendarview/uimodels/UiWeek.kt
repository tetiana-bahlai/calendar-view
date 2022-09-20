package com.tbahlai.calendarview.uimodels

data class UiWeek(
    val days: List<UiDay>,
    val events: List<UiWeekEvent>,
)