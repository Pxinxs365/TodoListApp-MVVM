package com.pxinxs.todolistapp.presentation.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pxinxs.todolistapp.R
import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.data.repositories.TasksRepository
import com.pxinxs.todolistapp.databinding.FragmentTasksBinding
import com.pxinxs.todolistapp.presentation.details.TaskDetailsFragment
import com.pxinxs.todolistapp.presentation.gone
import com.pxinxs.todolistapp.presentation.visible

class TasksFragment : Fragment(), TasksView, TasksRouter {

    // should be replaced with DI, made for simplicity
    private val tasksRepository: ITasksRepository by lazy { TasksRepository.getInstance() }
    private val presenter by lazy { TasksPresenter(tasksRepository) }

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        binding.fabAddTask.setOnClickListener { presenter.onAddTaskClicked() }

        presenter.onTakeView(this)
        presenter.onTakeRouter(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.onDropView()
        presenter.onDropRouter()
    }

    override fun showTasks(tasks: List<TasksAdapter.UiTask>) {
        binding.tvEmptyList.gone()
        binding.rvTasks.apply {
            adapter = TasksAdapter(tasks) { taskId -> presenter.onTaskClicked(taskId) }
            visible()
        }
    }

    override fun showNoTasks() {
        binding.tvEmptyList.visible()
        binding.rvTasks.gone()
    }

    override fun navigateToCreateNewTask() {
        findNavController().navigate(R.id.action_taskListFragment_to_taskDetailsFragment)
    }

    override fun navigateToTaskDetails(taskId: String) {
        val args = Bundle().apply { putString(TaskDetailsFragment.KEY_TASK_ID, taskId) }
        findNavController().navigate(R.id.action_taskListFragment_to_taskDetailsFragment, args)
    }
}