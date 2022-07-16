package com.pxinxs.todolistapp.presentation.details

import com.pxinxs.todolistapp.presentation.base.BaseView

interface TaskDetailsView : BaseView {
    fun showCreateNewTask(defaultDateFormatted: String)
    fun showTaskDetails(title: String, description: String, formattedDate: String)
    fun showDatePickerDialog()
    fun showDate(formattedDate: String)
    fun showSaveButtonEnabled()
    fun showSaveButtonDisabled()
}