package com.tbahlai.calendarview.utils

import java.util.*

fun String.capitalized(): String {
    return this.lowercase().replaceFirstChar {
        when (it.isLowerCase()) {
            true -> it.titlecase(Locale.getDefault())
            false -> it.toString()
        }
    }
}