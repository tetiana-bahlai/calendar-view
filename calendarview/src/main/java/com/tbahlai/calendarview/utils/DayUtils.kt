package com.tbahlai.calendarview.utils

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import java.math.RoundingMode


@Composable
fun getDayAspectRatio(countWeeks: Int): Float {
    val width = Resources.getSystem().displayMetrics.widthPixels
    val height = Resources.getSystem().displayMetrics.heightPixels

    val screenAspectRatio = when (width < height) {
        true -> width / height.toFloat()
        false -> height / width.toFloat() * 2
    }
    val aspectRatio = when (countWeeks) {
        /**
         * In rare cases the number of weeks can be 4.
         * For example, February 2027. So we have to handle it: -0.15F
         */
        MIN_COUNT_WEEKS_IN_MONTH -> screenAspectRatio.roundWith(RoundingMode.UP) - 0.15F
        AVERAGE_COUNT_WEEKS_IN_MONTH -> screenAspectRatio.roundWith(RoundingMode.DOWN)
        else -> screenAspectRatio.roundWith(RoundingMode.UP)
    }
    return aspectRatio
}

@Composable
fun getScreenWidthDp(): Float {
    val width = Resources.getSystem().displayMetrics.widthPixels
    return width / LocalDensity.current.density
}

@Composable
fun getMaxShownEventsCount(countWeeks: Int) : Int {
    val density = LocalDensity.current.density
    return when (countWeeks) {
        MIN_COUNT_WEEKS_IN_MONTH -> (density * 1.82F).toInt()
        AVERAGE_COUNT_WEEKS_IN_MONTH -> (density * 1.82F).toInt() - 1
        else -> (density * 1.82F).toInt() - 2
    }
}

fun Float.roundWith(roundMode: RoundingMode): Float {
    return this.toBigDecimal().setScale(1, roundMode).toFloat()
}