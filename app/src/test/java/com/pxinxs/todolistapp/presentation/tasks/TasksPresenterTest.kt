package com.pxinxs.todolistapp.presentation.tasks

import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.domain.models.Task
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.kotlin.whenever

internal class TasksPresenterTest {

    private val view = mock(TasksView::class.java)
    private val router = mock(TasksRouter::class.java)
    private val tasksRepository = mock(ITasksRepository::class.java)

    private lateinit var presenter: TasksPresenter

    @Before
    fun setUp() {
        presenter = TasksPresenter(tasksRepository)
    }

    @Test
    fun viewAttached_showTasksListIsEmpty() {
        // PREPARE
        whenever(tasksRepository.getTasks()).thenReturn(listOf())

        // PERFORM
        presenter.onTakeView(view)

        // CHECK
        verify(view).showNoTasks()
        verify(view, never()).showTasks(anyList())
    }

    @Test
    fun viewAttached_showTasks() {
        // PREPARE
        whenever(tasksRepository.getTasks()).thenReturn(listOf(task1, task2))

        // PERFORM
        presenter.onTakeView(view)

        // CHECK
        verify(view).showTasks(listOf(uiTask1, uiTask2))
        verify(view, never()).showNoTasks()
    }

    @Test
    fun onAddTaskClicked_navigateToCreateNewTask() {
        // PREPARE
        presenter.onTakeRouter(router)

        // PREFORM
        presenter.onAddTaskClicked()

        // CHECK
        verify(router).navigateToCreateNewTask()
    }

    @Test
    fun onTaskClicked_navigateToTaskDetails() {
        // PREPARE
        val taskId = "123"
        presenter.onTakeRouter(router)

        // PERFORM
        presenter.onTaskClicked(taskId)

        // CHECK
        verify(router).navigateToTaskDetails(taskId)
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