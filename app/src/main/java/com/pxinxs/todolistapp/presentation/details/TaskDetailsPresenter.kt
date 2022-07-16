package com.pxinxs.todolistapp.presentation.details

import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.domain.models.Task
import com.pxinxs.todolistapp.presentation.base.BasePresenter
import com.pxinxs.todolistapp.presentation.utils.DateFormatter
import com.pxinxs.todolistapp.presentation.utils.dateprovider.IDateProvider
import com.pxinxs.todolistapp.presentation.utils.uuidgenerator.IUuidGenerator

class TaskDetailsPresenter(
    private val dateProvider: IDateProvider,
    private val uuidGenerator: IUuidGenerator,
    private val tasksRepository: ITasksRepository
) : BasePresenter<TaskDetailsView, TaskDetailsRouter>() {

    private var taskId: String? = null
    private var dateInMillis: Long = 0

    fun setTaskId(taskId: String?) {
        this.taskId = taskId
    }

    override fun onTakeView(view: TaskDetailsView) {
        super.onTakeView(view)
        val task = tasksRepository.getTask(taskId)
        if (task == null) {
            handleNewTask(view)
        } else {
            handleEditTask(view, task)
        }
    }

    private fun handleNewTask(view: TaskDetailsView) {
        val todayDateInMillis = dateProvider.provideTodayDateInMillis()
        dateInMillis = todayDateInMillis
        val todayDateFormatted = DateFormatter.getFormattedDate(todayDateInMillis)
        view.showCreateNewTask(todayDateFormatted)
    }

    private fun handleEditTask(view: TaskDetailsView, task: Task) {
        view.showTaskDetails(
            task.title,
            task.description,
            DateFormatter.getFormattedDate(task.dateInMillis)
        )
    }

    fun restoreDate(date: String) {
        dateProvider.getDateInMillisFromString(date)?.let { restoredDateInMillis ->
            dateInMillis = restoredDateInMillis
        }
        view?.showDate(date)
    }

    fun onSaveButtonClicked(title: String, description: String) {
        if (taskId == null) {
            // create as new task
            val id = uuidGenerator.getNewUuid()
            val task = Task(id, title, description, dateInMillis)
            tasksRepository.saveTask(task)
        } else {
            tasksRepository.updateTaskDate(taskId, dateInMillis)
        }
        router?.closeScreen()
    }

    fun onDateClicked() {
        view?.showDatePickerDialog()
    }

    fun onDateSelected(dateInMillis: Long) {
        this.dateInMillis = dateInMillis
        view?.showDate(DateFormatter.getFormattedDate(dateInMillis))
    }

    fun onTaskChanged(title: String, description: String) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            view?.showSaveButtonEnabled()
        } else {
            view?.showSaveButtonDisabled()
        }
    }
}