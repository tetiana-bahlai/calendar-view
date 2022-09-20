package com.tbahlai.calendarview.month

import java.time.YearMonth

interface MonthInteractor {
    fun monthChanged(currentMonth: YearMonth)
}