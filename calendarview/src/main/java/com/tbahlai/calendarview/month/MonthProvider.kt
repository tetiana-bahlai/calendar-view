package com.tbahlai.calendarview.month

import androidx.compose.runtime.mutableStateMapOf
import com.tbahlai.calendarview.utils.getNextIndex
import com.tbahlai.calendarview.utils.getPreviousIndex
import com.tbahlai.calendarview.utils.next
import com.tbahlai.calendarview.utils.previous
import java.time.YearMonth

class MonthProvider(
    val currentMonth: YearMonth,
    val currentIndex: Int
) {
    val cache = mutableStateMapOf<Int, YearMonth>()

    init {
        cache[getNextIndex(currentIndex)] = currentMonth.next()
        cache[currentIndex] = currentMonth
        cache[getPreviousIndex(currentIndex)] = currentMonth.previous()
    }

    fun getMonthBy(index: Int): YearMonth {
        val month = cache[index]
        requireNotNull(month) { "month can't be null" }
        return month
    }
}