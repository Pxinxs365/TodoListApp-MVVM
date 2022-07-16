package com.pxinxs.todolistapp.presentation.tasks

import com.pxinxs.todolistapp.data.repositories.TasksRepository
import com.pxinxs.todolistapp.domain.models.Task
import com.pxinxs.todolistapp.presentation.tasks.TasksViewModel.TaskNavigationAction
import com.pxinxs.todolistapp.utils.TestCoroutineRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class TasksViewModelTest {

    private val tasksRepository = TasksRepository()

    private lateinit var viewModel: TasksViewModel

    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        viewModel = TasksViewModel(tasksRepository)
    }

    @Test
    fun viewCreated_showTasks() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.tasksFlow.collect() }

        // initial data
        assertEquals(listOf<TasksAdapter.UiTask>(), viewModel.tasksFlow.value)

        tasksRepository.saveTask(task1)
        assertEquals(listOf(uiTask1), viewModel.tasksFlow.value)

        tasksRepository.saveTask(task2)
        assertEquals(listOf(uiTask1, uiTask2), viewModel.tasksFlow.value)

        collectJob.cancel()
    }

    @Test
    fun onAddTaskClicked_navigateToCreateNewTask() = runTest {
        viewModel.onAddTaskClicked()

        assertTrue(viewModel.navigationActions.first() is TaskNavigationAction.NavigateToTaskDetails)
    }

    @Test
    fun onAddTaskClicked_navigateToTaskDetails() = runTest {
        val taskId = "123"
        viewModel.onTaskClicked(taskId)

        assertEquals(
            taskId,
            (viewModel.navigationActions.first() as TaskNavigationAction.NavigateToTaskDetails).taskId
        )
    }

    private val task1 = Task(
        id = "123",
        title = "Task 1",
        description = "Make project",
        dateInMillis = 1657918800000L
    )
    private val task2 = task1.copy(
        id = "456",
        title = "Task 2",
        description = "Launch project"
    )

    private val uiTask1 = TasksAdapter.UiTask(
        id = "123",
        title = "Task 1",
        description = "Make project",
        formattedTime = "2022/07/16"
    )
    private val uiTask2 = uiTask1.copy(
        id = "456",
        title = "Task 2",
        description = "Launch project"
    )
}