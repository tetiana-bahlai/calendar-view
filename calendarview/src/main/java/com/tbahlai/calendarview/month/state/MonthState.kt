package com.tbahlai.calendarview.month.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import java.time.YearMonth

fun MonthState(initialMonth: YearMonth): MonthState {
    return MonthStateImpl(initialMonth)
}

@Stable
interface MonthState {
    var currentMonth: YearMonth

    companion object {
        @Suppress("FunctionName")
        fun Saver(): Saver<MonthState, String> {
            return Saver(
                save = { it.currentMonth.toString() },
                restore = { MonthState(YearMonth.parse(it)) }
            )
        }
    }
}

@Stable
private class MonthStateImpl(initialMonth: YearMonth) : MonthState {

    private var _currentMonth by mutableStateOf<YearMonth>(initialMonth)

    override var currentMonth: YearMonth
        get() = _currentMonth
        set(value) {
            _currentMonth = value
        }
}
