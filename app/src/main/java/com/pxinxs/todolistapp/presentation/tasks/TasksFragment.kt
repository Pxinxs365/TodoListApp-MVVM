package com.pxinxs.todolistapp.presentation.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.pxinxs.todolistapp.R
import com.pxinxs.todolistapp.databinding.FragmentTasksBinding
import com.pxinxs.todolistapp.presentation.details.TaskDetailsFragment
import com.pxinxs.todolistapp.presentation.gone
import com.pxinxs.todolistapp.presentation.utils.viewmodel.launchAndRepeatWithViewLifecycle
import com.pxinxs.todolistapp.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private val viewModel: TasksViewModel by viewModels()

    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddTask.setOnClickListener { viewModel.onAddTaskClicked() }

//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.tasksFlow.collect { tasks ->
//                    if (tasks.isEmpty()) {
//                        showNoTasks()
//                    } else {
//                        showTasks(tasks)
//                    }
//                }
//            }
//        }

        viewModel.tasksLiveData.observe(viewLifecycleOwner) {tasks ->
            if (tasks.isEmpty()) {
                showNoTasks()
            } else {
                showTasks(tasks)
            }
        }

        // Observe navigation events
        launchAndRepeatWithViewLifecycle {
            viewModel.navigationActions.collect { action ->
                if (action is TasksViewModel.TaskNavigationAction.NavigateToTaskDetails) {
                    navigateToTaskDetails(action.taskId)
                }
            }
        }
    }

    private fun showTasks(tasks: List<TasksAdapter.UiTask>) {
        binding.tvEmptyList.gone()
        binding.rvTasks.apply {
            adapter = TasksAdapter(tasks) { taskId -> viewModel.onTaskClicked(taskId) }
            visible()
        }
    }

    private fun showNoTasks() {
        binding.tvEmptyList.visible()
        binding.rvTasks.gone()
    }

    private fun navigateToTaskDetails(taskId: String?) {
        val args = Bundle().apply { putString(TaskDetailsFragment.KEY_TASK_ID, taskId) }
        findNavController().navigate(R.id.action_taskListFragment_to_taskDetailsFragment, args)
    }
}