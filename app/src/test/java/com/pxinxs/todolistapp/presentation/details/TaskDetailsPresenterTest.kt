package com.pxinxs.todolistapp.presentation.details

import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.domain.models.Task
import com.pxinxs.todolistapp.presentation.utils.DateFormatter
import com.pxinxs.todolistapp.presentation.utils.dateprovider.IDateProvider
import com.pxinxs.todolistapp.presentation.utils.uuidgenerator.IUuidGenerator
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class TaskDetailsPresenterTest {

    private val view = mock(TaskDetailsView::class.java)
    private val router = mock(TaskDetailsRouter::class.java)
    private val dateProvider = mock(IDateProvider::class.java)
    private val uuidGenerator = mock(IUuidGenerator::class.java)
    private val tasksRepository = mock(ITasksRepository::class.java)

    private lateinit var presenter: TaskDetailsViewModel

    @Before
    fun setUp() {
        presenter = TaskDetailsViewModel(dateProvider, uuidGenerator, tasksRepository)
        presenter.setTaskId(null)
        whenever(uuidGenerator.getNewUuid()).thenReturn(TEST_UUID)
    }

    @Test
    fun fragmentCreated_showCreateNewTask() {
        // PREPARE
        val todayDateInMillis = 6432522563747L
        whenever(dateProvider.provideTodayDateInMillis()).thenReturn(todayDateInMillis)
        val defaultDateFormatted = DateFormatter.getFormattedDate(todayDateInMillis)

        // PERFORM
        presenter.onTakeView(view)

        // CHECK
        verify(view).showCreateNewTask(defaultDateFormatted)
        verify(view, never()).showTaskDetails(anyString(), anyString(), anyString())
    }

    @Test
    fun fragmentCreated_showTaskDetails() {
        // PREPARE
        val taskId = "1234"
        whenever(tasksRepository.getTask(taskId)).thenReturn(task)
        presenter.setTaskId(taskId)

        // PERFORM
        presenter.onTakeView(view)

        // CHECK
        val formattedDate = DateFormatter.getFormattedDate(task.dateInMillis)
        verify(view).showTaskDetails(task.title, task.description, formattedDate)
        verify(view, never()).showCreateNewTask(anyString())
    }

    @Test
    fun onSaveButtonClicked_saveNewTask() {
        // PREPARE
        presenter.onTakeView(view)
        presenter.onTakeRouter(router)
        val dataInMillis = 345678345L
        presenter.onDateSelected(dataInMillis)

        // PERFORM
        val title = "Todo title"
        val description = "Todo description"
        presenter.onSaveButtonClicked(title, description)

        // CHECK
//        verify(tasksRepository).saveTask(Task(TEST_UUID, title, description, dataInMillis))
        verify(router).closeScreen()
    }

    @Test
    fun onSaveButtonClicked_saveNewTaskWithDefaultDate() {
        // PREPARE
        val todayDateInMillis = 68452925928L
        whenever(dateProvider.provideTodayDateInMillis()).thenReturn(todayDateInMillis)
        presenter.onTakeView(view)
        presenter.onTakeRouter(router)

        // PERFORM
        val title = "Todo title"
        val description = "Todo description"
        presenter.onSaveButtonClicked(title, description)

        // CHECK
//        verify(tasksRepository).saveTask(
//            Task(
//                TEST_UUID,
//                title,
//                description,
//                todayDateInMillis
//            )
//        )
        verify(router).closeScreen()
    }

    @Test
    fun onSaveButtonClicked_updateTask() {
        // PREPARE
        val taskId = "1234"
        presenter.setTaskId(taskId)
        presenter.onTakeView(view)
        presenter.onTakeRouter(router)
        val dataInMillis = 3084630996L
        presenter.onDateSelected(dataInMillis)

        // PERFORM
        val title = "Todo title"
        val description = "Todo description"
        presenter.onSaveButtonClicked(title, description)

        // CHECK
        verify(tasksRepository).updateTaskDate(taskId, dataInMillis)
        verify(router).closeScreen()
    }

    @Test
    internal fun onDateClicked_showDatePickerDialog() {
        // PREPARE
        presenter.onTakeView(view)

        // PERFORM
        presenter.onDateClicked()

        // CHECK
        verify(view).showDatePickerDialog()
    }

    @Test
    internal fun onDateSelected_showDate() {
        // PREPARE
        presenter.onTakeView(view)

        // PERFORM
        val dateInMillis = 44526437L
        presenter.onDateSelected(dateInMillis)

        // CHECK
        val formattedDate = DateFormatter.getFormattedDate(dateInMillis)
        verify(view).showDate(formattedDate)
    }

    @Test
    internal fun titleIsEmpty_disableSaveButton() {
        // PREPARE
        presenter.onTakeView(view)

        // PERFORM
        presenter.onTaskChanged("", "description")

        // CHECK
        verify(view).showSaveButtonDisabled()
        verify(view, never()).showSaveButtonEnabled()
    }

    @Test
    internal fun descriptionIsEmpty_disableSaveButton() {
        // PREPARE
        presenter.onTakeView(view)

        // PERFORM
        presenter.onTaskChanged("title", "")

        // CHECK
        verify(view).showSaveButtonDisabled()
        verify(view, never()).showSaveButtonEnabled()
    }

    @Test
    internal fun titleAndDescriptionAreNotEmpty_enableSaveButton() {
        // PREPARE
        presenter.onTakeView(view)

        // PERFORM
        presenter.onTaskChanged("title", "desciption")

        // CHECK
        verify(view).showSaveButtonEnabled()
        verify(view, never()).showSaveButtonDisabled()
    }

    @Test
    internal fun restoreDate_showRestoredDate() {
        // PREPARE
        presenter.onTakeView(view)
        val date = "2022/07/15"

        // PERFORM
        presenter.restoreDate(date)

        // CHECK
        verify(view).showDate(date)
    }

    private val task = Task(
        id = "1234",
        title = "My first todo task",
        description = "To see the ocean",
        dateInMillis = 123456789L
    )

    private companion object {
        const val TEST_UUID = "1234-5678-90"
    }
}