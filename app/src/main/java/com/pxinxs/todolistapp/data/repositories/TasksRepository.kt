package com.pxinxs.todolistapp.data.repositories

import com.pxinxs.todolistapp.domain.models.Task
import java.util.concurrent.CopyOnWriteArrayList

class TasksRepository : ITasksRepository {

    private val tasks: MutableList<Task> = CopyOnWriteArrayList()

    override fun saveTask(task: Task) {
        tasks.add(task)
    }

    override fun getTask(id: String?): Task? =
        tasks.firstOrNull { it.id == id }

    override fun getTasks(): List<Task> = tasks

    override fun updateTaskDate(taskId: String?, dateInMillis: Long) {
        tasks.find { it.id == taskId }?.dateInMillis = dateInMillis
    }

    companion object {
        private var instance: TasksRepository? = null
        fun getInstance(): TasksRepository = synchronized(this) {
            if (instance == null) {
                instance = TasksRepository()
            }
            return instance as TasksRepository
        }
    }
}