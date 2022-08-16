package com.pxinxs.todolistapp.presentation.utils.dateprovider

import com.pxinxs.todolistapp.presentation.utils.DateFormatter
import java.util.Calendar
import javax.inject.Inject

class DateProvider @Inject constructor() : IDateProvider {

    override fun provideTodayDateInMillis() = Calendar.getInstance().timeInMillis

    override fun getDateInMillisFromString(date: String): Long? =
        DateFormatter.getDateInMillisFromString(date)
}