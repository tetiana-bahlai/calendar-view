package com.tbahlai.calendarview.uimodels

import com.tbahlai.calendarview.uimodels.UiEvent

data class UiWeekEvent(
    val event: UiEvent,
    val startEventDay: Long,
    val countDays: Long,
    val indexTop: Int
)