package com.tbahlai.calendarview.utils

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

internal fun Int.toIndex(startIndex: Int = START_INDEX, pageCount: Int = PAGES_COUNT): Int {
    return ((this - startIndex).floorMod(pageCount) + 1).mod(pageCount)
}

private fun Int.floorMod(other: Int): Int {
    return when (other) {
        0 -> this
        else -> this - floorDiv(other) * other
    }
}

@OptIn(ExperimentalPagerApi::class)
fun PagerState.getCurrentIndex(): Int {
    return currentPage.toIndex()
}

fun getNextIndex(currentIndex: Int) : Int {
    return (currentIndex + 1) % PAGES_COUNT
}

fun getPreviousIndex(currentIndex: Int) : Int {
    return (currentIndex - 1 + PAGES_COUNT) % PAGES_COUNT
}