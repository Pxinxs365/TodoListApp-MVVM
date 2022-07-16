package com.pxinxs.todolistapp.presentation.tasks

import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.presentation.base.BasePresenter
import com.pxinxs.todolistapp.presentation.utils.DateFormatter

class TasksPresenter(private val tasksRepository: ITasksRepository) :
    BasePresenter<TasksView, TasksRouter>() {

    override fun onTakeView(view: TasksView) {
        super.onTakeView(view)
        val tasks = tasksRepository.getTasks()
        if (tasks.isNotEmpty()) {
            val uiTasks = tasks.map { task ->
                val formattedDate = DateFormatter.getFormattedDate(task.dateInMillis)
                TasksAdapter.UiTask(
                    task.id,
                    task.title,
                    task.description,
                    formattedDate
                )
            }
            view.showTasks(uiTasks)
        } else {
            view.showNoTasks()
        }
    }

    fun onAddTaskClicked() {
        router?.navigateToCreateNewTask()
    }

    fun onTaskClicked(taskId: String) {
        router?.navigateToTaskDetails(taskId)
    }
}