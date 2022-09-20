package com.tbahlai.calendarview.uimodels

import androidx.compose.ui.graphics.Color
import java.time.Instant

data class UiEvent(
    val color: Color,
    val name: String,
    val startDate: Instant,
    val endDate: Instant,
)
