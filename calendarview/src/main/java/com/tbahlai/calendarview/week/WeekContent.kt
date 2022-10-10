package com.tbahlai.calendarview.week

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbahlai.calendarview.day.DayView
import com.tbahlai.calendarview.month.state.CalendarState
import com.tbahlai.calendarview.uimodels.UiWeek
import com.tbahlai.calendarview.utils.COUNT_DAYS_IN_WEEK
import com.tbahlai.calendarview.utils.getScreenWidthDp

@Composable
fun WeekContent(
    modifier: Modifier = Modifier,
    calendarState: CalendarState,
    dayAspectRatio: Float,
    countEvents: Int,
    eventClicked: (Long) -> Unit,
    week: UiWeek,
    dotsColor: Color,
    todayColor: Color,
    borderColor: Color,
    currentMonthDaysTextColor: Color,
    otherMonthDaysTextColor: Color
) {
    Box {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val events = week.events.distinctBy { it.indexTop }
            week.days.forEachIndexed { index, day ->
                Box(modifier = Modifier.fillMaxWidth(1f / (COUNT_DAYS_IN_WEEK - index))) {
                    DayView(
                        day = day,
                        dayHeight = events.size * 18 + 32,
                        dayAspectRatio = dayAspectRatio,
                        calendarModeState = calendarState.modeState,
                        todayColor = todayColor,
                        borderColor = borderColor,
                        currentMonthDaysTextColor = currentMonthDaysTextColor,
                        otherMonthDaysTextColor = otherMonthDaysTextColor
                    )
                }
            }
        }

        val widthOneDay = getScreenWidthDp() / COUNT_DAYS_IN_WEEK
        val isExistMoreEvents = week.events.find { it.indexTop > countEvents } != null
        val needToShowMore = isExistMoreEvents && calendarState.modeState.isCalendarMonthMode()
        val eventsList = when (!calendarState.modeState.isCalendarMonthMode()) {
            true -> week.events
            false -> week.events.filterNot { it.indexTop > countEvents }
        }

        Box(modifier = Modifier.padding(top = 26.dp)) {
            eventsList.forEachIndexed { index, weekEvent ->
                val startPadding = ((widthOneDay * weekEvent.startEventDay.toInt()).dp + 2.dp)
                Text(
                    modifier = Modifier
                        .padding(
                            top = (weekEvent.indexTop * 18).dp,
                            start = startPadding,
                            end = 4.dp
                        )
                        .width((weekEvent.countDays.toInt() * widthOneDay).dp - 4.dp)
                        .clickable { eventClicked(weekEvent.event.id) }
                        .background(weekEvent.event.color, RoundedCornerShape(4.dp))
                        .padding(start = 4.dp, end = 4.dp, top = 1.dp, bottom = 1.dp),
                    text = weekEvent.event.name,
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                val minEventsCount = if (countEvents - 1 >= 0) countEvents - 1 else 0
                if (needToShowMore && eventsList[index].indexTop == minEventsCount) {
                    LazyRow(
                        Modifier
                            .padding(
                                top = ((eventsList[index].indexTop + 2) * 20 - 4).dp,
                                start = startPadding + 4.dp,
                                end = 4.dp
                            )
                    ) {
                        items(3) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 4.dp, end = 4.dp)
                                    .width(4.dp)
                                    .height(4.dp)
                                    .background(dotsColor, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}