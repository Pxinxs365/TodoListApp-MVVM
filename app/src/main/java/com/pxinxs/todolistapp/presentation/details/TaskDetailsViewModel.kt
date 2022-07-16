package com.pxinxs.todolistapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.domain.models.Task
import com.pxinxs.todolistapp.presentation.utils.viewmodel.WhileViewSubscribed
import com.pxinxs.todolistapp.presentation.utils.dateprovider.IDateProvider
import com.pxinxs.todolistapp.presentation.utils.uuidgenerator.IUuidGenerator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    private val dateProvider: IDateProvider,
    private val uuidGenerator: IUuidGenerator,
    private val tasksRepository: ITasksRepository
) : ViewModel() {

    val taskId2: MutableStateFlow<String?> = MutableStateFlow(null)

    val task: SharedFlow<Task?> = taskId2.transform { taskId ->
        emit(tasksRepository.getTask(taskId))
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _dateInMillis: MutableStateFlow<Long> =
        MutableStateFlow(dateProvider.provideTodayDateInMillis())
    val dateInMillis: StateFlow<Long> = _dateInMillis

    private val _navigationActions = Channel<TaskNavigationAction>(capacity = Channel.CONFLATED)
    val navigationActions: Flow<TaskNavigationAction> = _navigationActions.receiveAsFlow()

    private val _saveButtonEnabled = Channel<Boolean>(capacity = Channel.RENDEZVOUS)
    val saveButtonEnabled: Flow<Boolean> = _saveButtonEnabled.receiveAsFlow()

    fun onTaskChanged(title: String, description: String) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            _saveButtonEnabled.trySend(true)
        } else {
            _saveButtonEnabled.trySend(false)
        }
    }

    fun onDateSelected(dateInMillis: Long) {
        _dateInMillis.value = dateInMillis
    }

    fun onSaveButtonClicked(title: String, description: String) {
        if (taskId2.value == null) {
            // create as new task
            val id = uuidGenerator.getNewUuid()
            val task = Task(id, title, description, dateInMillis.value)

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    tasksRepository.saveTask(task)
                }
            }
        } else {
            tasksRepository.updateTaskDate(taskId2.value, dateInMillis.value)
        }
        _navigationActions.trySend(TaskNavigationAction.CloseFragment)
    }

    sealed class TaskNavigationAction {
        object CloseFragment : TaskNavigationAction()
    }
}
