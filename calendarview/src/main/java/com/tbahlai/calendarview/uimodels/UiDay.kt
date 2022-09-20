package com.tbahlai.calendarview.uimodels

import java.time.LocalDate

data class UiDay constructor(
    val date: LocalDate,
    val weekCount: Int,
    val isCurrentDay: Boolean,
    val isFromCurrentMonth: Boolean
)
