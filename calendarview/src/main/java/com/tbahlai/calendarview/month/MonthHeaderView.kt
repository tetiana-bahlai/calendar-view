package com.tbahlai.calendarview.month

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbahlai.calendarview.month.state.MonthState
import com.tbahlai.calendarview.utils.capitalized
import com.tbahlai.calendarview.utils.getYears
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth

@Composable
fun MonthHeaderView(
    yearsInterval: Pair<Long, Long>,
    monthState: MonthState,
    todayColor: Color,
    monthHeaderTextColor: Color,
    monthInteractor: MonthInteractor
) {
    val currentMonth = monthState.currentMonth.month.toString().capitalized()
    val currentYear = monthState.currentMonth.year

    var isYearDropDownShown by remember { mutableStateOf(false) }
    var isMonthDropDownShown by remember { mutableStateOf(false) }

    Row(Modifier.padding(16.dp)) {
        Column {
            Text(
                modifier = Modifier.clickable { isYearDropDownShown = !isYearDropDownShown },
                text = currentYear.toString(),
                fontSize = 22.sp,
                color = monthHeaderTextColor
            )

            if (isYearDropDownShown) {
                DropdownMenu(
                    expanded = isYearDropDownShown,
                    onDismissRequest = { isYearDropDownShown = false }) {
                    val listOfYears = getYears(yearsInterval.first, yearsInterval.second)
                    listOfYears.forEach {
                        DropdownMenuItem(onClick = {
                            monthState.currentMonth = YearMonth.of(it.value, monthState.currentMonth.month)
                            monthInteractor.monthChanged(monthState.currentMonth)
                            isYearDropDownShown = false
                        }) {
                            Text(text = it.toString())
                        }
                    }
                }
            }

        }

        Column(modifier = Modifier.weight(1F)) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { isMonthDropDownShown = !isMonthDropDownShown },
                text = currentMonth,
                fontSize = 22.sp,
                color = monthHeaderTextColor
            )

            if (isMonthDropDownShown) {
                DropdownMenu(
                    expanded = isMonthDropDownShown,
                    onDismissRequest = { isMonthDropDownShown = false }) {
                    val listOfMonth = Month.values()
                    listOfMonth.forEach {
                        DropdownMenuItem(onClick = {
                            monthState.currentMonth = YearMonth.of(currentYear, it)
                            monthInteractor.monthChanged(monthState.currentMonth)
                            isMonthDropDownShown = false
                        }) {
                            Text(text = it.name.capitalized())
                        }
                    }
                }
            }
        }

        val isCurrentMonth = YearMonth.now().month == monthState.currentMonth.month
        val isCurrentYear = Year.now().value == monthState.currentMonth.year

        if (!isCurrentMonth || !isCurrentYear) {
            Box(
                modifier = Modifier
                    .background(todayColor, CircleShape)
                    .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                    .clickable {
                        monthState.currentMonth = YearMonth.now()
                        monthInteractor.monthChanged(monthState.currentMonth)
                    }
            ) {
                Text(text = LocalDate.now().dayOfMonth.toString(), fontSize = 16.sp)
            }
        }
    }
}