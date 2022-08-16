package com.pxinxs.todolistapp.data.repositories

import com.pxinxs.todolistapp.domain.models.Task
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksRepository @Inject constructor() : ITasksRepository {

    private val tasks: MutableList<Task> = CopyOnWriteArrayList()
    private val tasksFlow: MutableSharedFlow<List<Task>> = MutableSharedFlow()

    override suspend fun saveTask(task: Task) {
        tasks.add(task)
        tasksFlow.emit(tasks)
    }

    override fun getTask(id: String?): Task? =
        tasks.firstOrNull { it.id == id }

    override fun getTasks(): List<Task> = tasks

    override fun observeTasks() = tasksFlow.asSharedFlow()

    override fun updateTaskDate(taskId: String?, dateInMillis: Long) {
        tasks.find { it.id == taskId }?.dateInMillis = dateInMillis
    }
}