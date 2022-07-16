package com.pxinxs.todolistapp.data.repositories

import com.pxinxs.todolistapp.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface ITasksRepository {
    suspend fun saveTask(task: Task)
    fun getTask(id: String?): Task?
    fun getTasks(): List<Task>
    fun observeTasks(): Flow<List<Task>>
    fun updateTaskDate(taskId: String?, dateInMillis: Long)
}