package com.tbahlai.calendarview.month

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.tbahlai.calendarview.month.state.CalendarState
import com.tbahlai.calendarview.month.state.MonthPagerState
import com.tbahlai.calendarview.month.state.MonthState
import com.tbahlai.calendarview.uimodels.CalendarMode
import com.tbahlai.calendarview.uimodels.UiEvent
import com.tbahlai.calendarview.utils.*
import com.tbahlai.calendarview.week.WeekContent
import com.tbahlai.calendarview.week.WeekHeaderView
import com.tbahlai.calendarview.week.getWeeks
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonthPager(
    modifier: Modifier = Modifier,
    calendarState: CalendarState,
    events: List<UiEvent>,
    eventClicked: (Long) -> Unit,
    monthClicked: (YearMonth) -> Unit,
    monthState: MonthState,
    daysOfWeek: List<DayOfWeek>,
    dotsColor: Color,
    todayColor: Color,
    borderColor: Color,
    currentMonthDaysTextColor: Color,
    otherMonthDaysTextColor: Color,
    dayOfWeekTextColor: Color,
    calendarModeChanged: (CalendarMode, LocalDate, LocalDate) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = START_INDEX)
    val coroutineScope = rememberCoroutineScope()

    val monthPagerState = remember {
        MonthPagerState(
            coroutineScope = coroutineScope,
            monthState = monthState,
            pagerState = pagerState,
        ) { monthClicked(it) }
    }

    if (calendarState.modeState.isCalendarMonthMode()) {
        HorizontalPager(
            modifier = modifier.fillMaxSize(),
            count = PAGER_ITEM_COUNT,
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) {
            MonthContent(
                events = events,
                daysOfWeek = daysOfWeek,
                currentMonth = monthPagerState.getMonthForPage(it.toIndex()),
                eventClicked = eventClicked,
                calendarState = calendarState,
                dotsColor = dotsColor,
                todayColor = todayColor,
                borderColor = borderColor,
                currentMonthDaysTextColor = currentMonthDaysTextColor,
                otherMonthDaysTextColor = otherMonthDaysTextColor,
                dayOfWeekTextColor = dayOfWeekTextColor,
                calendarModeChanged = calendarModeChanged
            )

        }
    } else {
        MonthContent(
            events = events,
            daysOfWeek = daysOfWeek,
            currentMonth = calendarState.monthState.currentMonth,
            eventClicked = eventClicked,
            calendarState = calendarState,
            dotsColor = dotsColor,
            todayColor = todayColor,
            borderColor = borderColor,
            currentMonthDaysTextColor = currentMonthDaysTextColor,
            otherMonthDaysTextColor = otherMonthDaysTextColor,
            dayOfWeekTextColor = dayOfWeekTextColor,
            calendarModeChanged = calendarModeChanged
        )
    }

}

@Composable
fun MonthContent(
    modifier: Modifier = Modifier,
    calendarState: CalendarState,
    eventClicked: (Long) -> Unit,
    calendarModeChanged: (CalendarMode, LocalDate, LocalDate) -> Unit,
    events: List<UiEvent>,
    daysOfWeek: List<DayOfWeek>,
    currentMonth: YearMonth,
    dotsColor: Color,
    todayColor: Color,
    borderColor: Color,
    currentMonthDaysTextColor: Color,
    otherMonthDaysTextColor: Color,
    dayOfWeekTextColor: Color
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            content = { WeekHeaderView(daysOfWeek = daysOfWeek, dayOfWeekTextColor = dayOfWeekTextColor) },
        )

        Column(modifier = Modifier.wrapContentWidth()) {
            val weeks = currentMonth.getWeeks(firstDayOfWeek = daysOfWeek.first(), events = events)
            val aspectRatio = getDayAspectRatio(countWeeks = weeks.size)
            val countShownEvents = getMaxShownEventsCount(weeks.size)

            if (calendarState.modeState.isCalendarMonthMode()) {
                weeks.forEach {
                    WeekContent(
                        week = it,
                        dayAspectRatio = aspectRatio,
                        eventClicked = eventClicked,
                        calendarState = calendarState,
                        countEvents = countShownEvents,
                        dotsColor = dotsColor,
                        todayColor = todayColor,
                        borderColor = borderColor,
                        currentMonthDaysTextColor = currentMonthDaysTextColor,
                        otherMonthDaysTextColor = otherMonthDaysTextColor,
                        calendarModeChanged = calendarModeChanged
                    )
                }
            } else {
                val week = when (calendarState.modeState.getWeekCount() > weeks.size - 1) {
                    true -> weeks.last()
                    false -> weeks[calendarState.modeState.getWeekCount()]
                }
                WeekContent(
                    week = week,
                    dayAspectRatio = aspectRatio,
                    eventClicked = eventClicked,
                    calendarState = calendarState,
                    countEvents = countShownEvents,
                    dotsColor = dotsColor,
                    todayColor = todayColor,
                    borderColor = borderColor,
                    currentMonthDaysTextColor = currentMonthDaysTextColor,
                    otherMonthDaysTextColor = otherMonthDaysTextColor,
                    calendarModeChanged = calendarModeChanged
                )
            }
        }
    }
}