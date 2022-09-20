package com.tbahlai.calendarview.week

import com.tbahlai.calendarview.uimodels.UiDay
import com.tbahlai.calendarview.uimodels.UiEvent
import com.tbahlai.calendarview.uimodels.UiWeek
import com.tbahlai.calendarview.uimodels.UiWeekEvent
import com.tbahlai.calendarview.utils.*
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth

fun YearMonth.getWeeks(firstDayOfWeek: DayOfWeek, events: List<UiEvent>): List<UiWeek> {
    val countDaysInCurrMonth = lengthOfMonth()
    val firstDayOfCurrMonth = atDay(1).dayOfWeek.value
    val lastDayOfCurrMonth = atDay(countDaysInCurrMonth).dayOfWeek.value

    val startDayOfPrevMonth = firstDayOfCurrMonth - firstDayOfWeek.value
    val lastDayOfPrevMonth = COUNT_DAYS_IN_WEEK - lastDayOfCurrMonth - (firstDayOfWeek.value - 1)

    val firstDayOfCalendar = 1 - startDayOfPrevMonth
    val lastDayOfCalendar = countDaysInCurrMonth + lastDayOfPrevMonth

    val weeks = mutableListOf<UiWeek>()

    val previousMonth = this.previous()
    val nextMonth = this.next()

    val firstVisibleDayInPrevMonth = previousMonth.lengthOfMonth() - startDayOfPrevMonth
    val lastVisibleDayInNextMonth =
        if (lastDayOfPrevMonth == 0) lastDayOfPrevMonth + 1 else lastDayOfPrevMonth

    val firstDateOfCalendar = previousMonth.atDay(firstVisibleDayInPrevMonth).toInstant()
    val lastDateOfCalendar = nextMonth.atDay(lastVisibleDayInNextMonth).toInstant()

    val allWeeksInCurrCalendar = (firstDayOfCalendar..lastDayOfCalendar).chunked(COUNT_DAYS_IN_WEEK)

    allWeeksInCurrCalendar.forEachIndexed { index, days ->
        val daysOfWeek = mutableListOf<UiDay>()

        daysOfWeek += days.map { dayOfMonth ->
            val (date, isFromCurrMonth) = when (dayOfMonth) {
                in Int.MIN_VALUE..0 -> {
                    previousMonth.atDay(previousMonth.lengthOfMonth() + dayOfMonth) to false
                }
                in 1..countDaysInCurrMonth -> atDay(dayOfMonth) to true
                else -> nextMonth.atDay(dayOfMonth - countDaysInCurrMonth) to false
            }
            UiDay(
                date = date,
                isFromCurrentMonth = isFromCurrMonth,
                isCurrentDay = date == LocalDate.now(),
                weekCount = index
            )
        }

        val eventsForCurrWeek = mutableListOf<UiWeekEvent>()

        val firstDayInWeek = daysOfWeek.first().date.toInstant()
        val lastDayInWeek = daysOfWeek.last().date.toInstant()

        val newEventsForCurrWeek = events.filter {
            isEventInCurrentWeek(it, firstDayInWeek, lastDayInWeek)
        }

        newEventsForCurrWeek.forEach { event ->
            val eventEndBeforeCurrMonth = event.endDate.isBefore(firstDateOfCalendar)
            val eventStartAfterCurrMonth = event.startDate.isAfter(lastDateOfCalendar)
            if (eventEndBeforeCurrMonth || eventStartAfterCurrMonth) return@forEach

            val eventEndAfterFirstDayWeek = event.endDate.isAfter(firstDayInWeek)
            val eventStartBeforeLastDayWeek = event.startDate.isBefore(lastDayInWeek)

            val eventEndAfterLastDayWeek = event.endDate.isAfter(lastDayInWeek)
            val eventStartBeforeFirstDayWeek = event.startDate.isBefore(firstDayInWeek)

            if (eventEndAfterFirstDayWeek || eventStartBeforeLastDayWeek) {
                val startEventDay = firstDayInWeek.daysBetween(event.startDate)
                val endEventDay = lastDayInWeek.daysBetween(event.endDate)

                val countDays = when {
                    eventEndAfterLastDayWeek && eventStartBeforeFirstDayWeek -> {
                        COUNT_DAYS_IN_WEEK.toLong()
                    }
                    eventEndAfterLastDayWeek -> COUNT_DAYS_IN_WEEK - startEventDay
                    eventStartBeforeFirstDayWeek -> COUNT_DAYS_IN_WEEK - endEventDay
                    else -> COUNT_DAYS_IN_WEEK - endEventDay - startEventDay
                }

                val indexTop = if (eventsForCurrWeek.isNotEmpty()) {
                    var currIndex = 0

                    val filtered = eventsForCurrWeek.filter {
                        isCurrentEventOverlapPrevious(event, it)
                    }

                    when (filtered.isNotEmpty()) {
                        true -> {
                            filtered.sortedBy { it.indexTop }.forEach {
                                if (it.indexTop == currIndex) currIndex += 1
                            }
                        }
                        false -> currIndex = eventsForCurrWeek.distinctBy { it.indexTop }.size
                    }
                    currIndex
                } else 0

                eventsForCurrWeek += UiWeekEvent(
                    event = event,
                    startEventDay = if (event.startDate.isAfter(firstDayInWeek)) startEventDay else 0,
                    countDays = countDays,
                    indexTop = indexTop
                )
            }
        }
        weeks += UiWeek(days = daysOfWeek, events = eventsForCurrWeek)
    }

    return weeks
}

fun isCurrentEventOverlapPrevious(newEvent: UiEvent, oldEvent: UiWeekEvent) : Boolean {
    return (oldEvent.event.startDate.isBefore(newEvent.startDate) && (oldEvent.event.endDate.isAfter(newEvent.endDate)) ||
            (oldEvent.event.startDate == newEvent.startDate && oldEvent.event.endDate.isAfter(newEvent.endDate)) ||
            (oldEvent.event.startDate.isBefore(newEvent.startDate) && oldEvent.event.endDate == newEvent.endDate) ||
            (oldEvent.event.startDate == newEvent.startDate && oldEvent.event.endDate == newEvent.endDate) ||
            (oldEvent.event.startDate.isAfter(newEvent.startDate) && newEvent.endDate == oldEvent.event.endDate) ||
            (oldEvent.event.startDate == newEvent.endDate) || (oldEvent.event.endDate == newEvent.startDate))
}

fun isEventInCurrentWeek(event: UiEvent, firstDayInWeek: Instant, lastDayInWeek: Instant): Boolean {
    return (event.startDate.isAfter(firstDayInWeek) && event.startDate.isBefore(lastDayInWeek)) ||
            (event.endDate.isAfter(firstDayInWeek) && event.endDate.isBefore(lastDayInWeek)) ||
            (event.startDate.isBefore(firstDayInWeek) && event.endDate.isAfter(lastDayInWeek) ||
            (event.startDate == firstDayInWeek && event.startDate.isBefore(lastDayInWeek) ||
            (event.startDate.isAfter(firstDayInWeek) && event.endDate == lastDayInWeek) ||
            (event.endDate == firstDayInWeek) || event.startDate == lastDayInWeek))
}