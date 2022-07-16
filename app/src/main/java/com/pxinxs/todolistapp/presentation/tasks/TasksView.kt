package com.pxinxs.todolistapp.presentation.tasks

import com.pxinxs.todolistapp.presentation.base.BaseView

interface TasksView: BaseView {
    fun showTasks(tasks: List<TasksAdapter.UiTask>)
    fun showNoTasks()
}