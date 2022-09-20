package com.tbahlai.calendarview.month.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import com.tbahlai.calendarview.uimodels.UiCalendarInfo
import com.tbahlai.calendarview.uimodels.CalendarMode

fun CalendarInfoState(initialCalendarInfo: UiCalendarInfo): CalendarInfoState {
    return CalendarInfoImpl(initialCalendarInfo)
}

@Stable
interface CalendarInfoState {
    var currentCalendarInfo: UiCalendarInfo

    fun isCalendarMonthMode() : Boolean {
        return CalendarMode.MONTH_MODE == currentCalendarInfo.calendarMode
    }

    fun getCalendarMode() : CalendarMode {
        return currentCalendarInfo.calendarMode
    }

    fun getWeekCount() : Int {
        return currentCalendarInfo.weekCount
    }

    fun setCalendarInfo(calendarMode: CalendarMode, weekCount: Int) {
        currentCalendarInfo = currentCalendarInfo.copy(calendarMode, weekCount)
    }

    companion object {
        @Suppress("FunctionName")
        fun Saver(): Saver<CalendarInfoState, String> {
            return Saver(
                save = { it.toString() },
                restore = { CalendarInfoState(UiCalendarInfo(CalendarMode.MONTH_MODE, 0)) }
            )
        }
    }
}

@Stable
private class CalendarInfoImpl(initialCalendarInfo: UiCalendarInfo) : CalendarInfoState {

    private var _currentCalendarInfo by mutableStateOf<UiCalendarInfo>(initialCalendarInfo)

    override var currentCalendarInfo: UiCalendarInfo
        get() = _currentCalendarInfo
        set(value) { _currentCalendarInfo = value }
}
