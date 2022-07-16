package com.pxinxs.todolistapp.data.repositories

import com.pxinxs.todolistapp.domain.models.Task

interface ITasksRepository {
    fun saveTask(task: Task)
    fun getTask(id: String?): Task?
    fun getTasks(): List<Task>
    fun updateTaskDate(taskId: String?, dateInMillis: Long)
}