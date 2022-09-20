package com.tbahlai.calendarview.week

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
fun WeekHeaderView(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek>,
    dayOfWeekTextColor: Color
) {
    Row(modifier = modifier.padding(top = 4.dp, bottom = 4.dp)) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = modifier
                    .weight(1f)
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = dayOfWeekTextColor
            )
        }
    }
}