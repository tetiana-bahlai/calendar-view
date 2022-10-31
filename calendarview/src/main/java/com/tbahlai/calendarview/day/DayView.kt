package com.tbahlai.calendarview.day

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbahlai.calendarview.month.state.CalendarInfoState
import com.tbahlai.calendarview.uimodels.CalendarMode
import com.tbahlai.calendarview.uimodels.UiDay

@Composable
fun DayView(
    day: UiDay,
    dayClicked: () -> Unit,
    dayHeight: Int,
    dayAspectRatio: Float,
    calendarModeState: CalendarInfoState,
    todayColor: Color,
    borderColor: Color,
    currentMonthDaysTextColor: Color,
    otherMonthDaysTextColor: Color
) {
    val modifier = when (calendarModeState.isCalendarMonthMode()) {
        true -> Modifier.aspectRatio(ratio = dayAspectRatio, matchHeightConstraintsFirst = true)
        false -> Modifier.height(dayHeight.dp)
    }

    Column(
        modifier = modifier
            .border(0.05.dp, borderColor)
            .background(if (day.isCurrentDay) todayColor else Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    val newCalendarMode = when (calendarModeState.getCalendarMode()) {
                        CalendarMode.MONTH_MODE -> CalendarMode.WEEK_MODE
                        CalendarMode.WEEK_MODE -> CalendarMode.MONTH_MODE
                    }
                    calendarModeState.setCalendarInfo(newCalendarMode, day.weekCount)
                    dayClicked()
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val textColor = when {
                    day.isFromCurrentMonth -> currentMonthDaysTextColor
                    else -> otherMonthDaysTextColor.copy(alpha = 0.3f)
                }

                Text(
                    modifier = Modifier,
                    text = day.date.dayOfMonth.toString(), color = textColor, fontSize = 20.sp
                )
            }
        }
    }
}