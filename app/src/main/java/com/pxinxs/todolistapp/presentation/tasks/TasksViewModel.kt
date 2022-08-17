package com.pxinxs.todolistapp.presentation.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.domain.models.Task
import com.pxinxs.todolistapp.presentation.utils.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    tasksRepository: ITasksRepository
) : ViewModel() {

//    val tasksFlow: StateFlow<List<TasksAdapter.UiTask>> = tasksRepository.observeTasks()
//        .map { tasks -> toUiTasks(tasks) }
//        .stateIn(viewModelScope, started = WhileViewSubscribed, initialValue = emptyList())

    val tasksLiveData: LiveData<List<TasksAdapter.UiTask>> = tasksRepository.observeTasks().map {
        tasks -> toUiTasks(tasks)
    }.asLiveData()

    private val _navigationActions = Channel<TaskNavigationAction>(capacity = CONFLATED)
    val navigationActions: Flow<TaskNavigationAction> = _navigationActions.receiveAsFlow()

    fun onAddTaskClicked() {
        _navigationActions.trySend(TaskNavigationAction.NavigateToTaskDetails(null))
    }

    fun onTaskClicked(taskId: String) {
        _navigationActions.trySend(TaskNavigationAction.NavigateToTaskDetails(taskId))
    }

    private fun toUiTasks(tasks: List<Task>) =
        tasks.map { task ->
            val formattedDate = DateFormatter.getFormattedDate(task.dateInMillis)
            TasksAdapter.UiTask(
                task.id,
                task.title,
                task.description,
                formattedDate
            )
        }

    sealed class TaskNavigationAction {
        class NavigateToTaskDetails(val taskId: String?) : TaskNavigationAction()
    }
}