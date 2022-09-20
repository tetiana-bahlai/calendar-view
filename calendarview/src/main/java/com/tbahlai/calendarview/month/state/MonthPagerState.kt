package com.tbahlai.calendarview.month.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.tbahlai.calendarview.month.MonthProvider
import com.tbahlai.calendarview.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import java.time.YearMonth

@OptIn(ExperimentalPagerApi::class)
class MonthPagerState(
    coroutineScope: CoroutineScope,
    private val monthState: MonthState,
    private val pagerState: PagerState,
    private val monthChanged : (YearMonth) -> Unit
) {

    private var monthProvider by mutableStateOf(
        MonthProvider(
            currentMonth = monthState.currentMonth,
            currentIndex = pagerState.getCurrentIndex(),
        )
    )

    init {
        snapshotFlow { monthState.currentMonth }
            .onEach { month -> moveToMonth(month) }
            .launchIn(coroutineScope)

        snapshotFlow { pagerState.getCurrentIndex() }
            .runningFold(1 to 1) { oldIndices, newIndex -> oldIndices.second to newIndex }
            .distinctUntilChanged()
            .onEach { (old, new) -> onScrolled(old, new) }
            .launchIn(coroutineScope)
    }

    fun getMonthForPage(index: Int): YearMonth {
        return monthProvider.getMonthBy(index)
    }

    private fun moveToMonth(month: YearMonth) {
        if (month == getMonthForPage(pagerState.getCurrentIndex())) return
        monthProvider = MonthProvider(this.monthState.currentMonth, pagerState.getCurrentIndex())
    }

    private fun onScrolled(oldIndex: Int, newIndex: Int) {
        if (oldIndex == newIndex) return

        val month = monthProvider.getMonthBy(newIndex)
        if (month.month != monthState.currentMonth.month) monthState.currentMonth = month

        when (oldIndex - newIndex) {
            MIN_INDEX, PAGES_COUNT - 1 -> monthProvider.cache[getNextIndex(newIndex)] = month.next()
            else -> monthProvider.cache[getPreviousIndex(newIndex)] = month.previous()
        }
        monthChanged(monthState.currentMonth)
    }
}