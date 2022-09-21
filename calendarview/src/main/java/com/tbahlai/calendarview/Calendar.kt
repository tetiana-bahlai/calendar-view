package com.tbahlai.calendarview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tbahlai.calendarview.month.MonthHeaderView
import com.tbahlai.calendarview.month.MonthInteractor
import com.tbahlai.calendarview.month.MonthPager
import com.tbahlai.calendarview.month.state.CalendarInfoState
import com.tbahlai.calendarview.month.state.CalendarState
import com.tbahlai.calendarview.month.state.MonthState
import com.tbahlai.calendarview.uimodels.CalendarMode
import com.tbahlai.calendarview.uimodels.UiCalendarInfo
import com.tbahlai.calendarview.uimodels.UiEvent
import com.tbahlai.calendarview.week.WeekInteractor
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun Calendar(
    events: List<UiEvent>,
    calendarState: CalendarState = rememberCalendarState(),
    weekInteractor: WeekInteractor,
    monthInteractor: MonthInteractor,
    yearsInterval: Pair<Long, Long> = Pair(1, 1),
    dots: Color = Color.Black,
    todayColor: Color = Color.LightGray,
    dropDownMenuTextColor: Color = Color.Black,
    borderColor: Color = Color.LightGray,
    currentMonthDaysTextColor: Color = Color.Black,
    otherMonthDaysTextColor: Color = Color.LightGray,
    dayOfWeekTextColor: Color = Color.Black,
    monthHeaderTextColor: Color = Color.Black,
    monthHeader: @Composable () -> Unit = {
        MonthHeaderView(
            yearsInterval = yearsInterval,
            monthState = calendarState.monthState,
            todayColor = todayColor,
            dropDownMenuTextColor = dropDownMenuTextColor,
            monthHeaderTextColor = monthHeaderTextColor,
            monthInteractor = monthInteractor
        )
    },
) {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    val daysOfWeek = remember(firstDayOfWeek) { DayOfWeek.values().toList() }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        monthHeader()

        MonthPager(
            events = events,
            daysOfWeek = daysOfWeek,
            monthState = calendarState.monthState,
            weekInteractor = weekInteractor,
            monthInteractor = monthInteractor,
            calendarState = calendarState,
            dotsColor = dots,
            todayColor = todayColor,
            borderColor = borderColor,
            currentMonthDaysTextColor = currentMonthDaysTextColor,
            otherMonthDaysTextColor = otherMonthDaysTextColor,
            dayOfWeekTextColor = dayOfWeekTextColor
        )
    }
}

@Composable
fun rememberCalendarState(): CalendarState {
    val monthState = rememberSaveable(saver = MonthState.Saver()) {
        MonthState(initialMonth = YearMonth.now())
    }

    val calendarModeState = rememberSaveable(saver = CalendarInfoState.Saver()) {
        CalendarInfoState(UiCalendarInfo(calendarMode = CalendarMode.MONTH_MODE, weekCount = 0))
    }
    return remember { CalendarState(monthState, calendarModeState) }
}