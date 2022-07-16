package com.pxinxs.todolistapp.presentation.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    /**
    return format "yyyy/MM/dd"
     */
    fun getFormattedDate(date: Long): String {
        return SimpleDateFormat(DATE_FORMATTING_PATTERN, Locale.getDefault()).format(date)
    }

    fun getDateInMillisFromString(date: String): Long? {
        val format = SimpleDateFormat(DATE_FORMATTING_PATTERN)
        return try {
            format.parse(date)?.time
        } catch (e: ParseException) {
            null
        }
    }

    private const val DATE_FORMATTING_PATTERN = "yyyy/MM/dd"
}