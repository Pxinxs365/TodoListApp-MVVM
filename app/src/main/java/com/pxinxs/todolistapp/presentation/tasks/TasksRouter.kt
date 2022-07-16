package com.pxinxs.todolistapp.presentation.tasks

import com.pxinxs.todolistapp.presentation.base.BaseRouter

interface TasksRouter: BaseRouter {
    fun navigateToCreateNewTask()
    fun navigateToTaskDetails(taskId: String)
}