package com.pxinxs.todolistapp.presentation.utils.dateprovider

interface IDateProvider {
    fun provideTodayDateInMillis(): Long
    fun getDateInMillisFromString(date: String): Long?
}