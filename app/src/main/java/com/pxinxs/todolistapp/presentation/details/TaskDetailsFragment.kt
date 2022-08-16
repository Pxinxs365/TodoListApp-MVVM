package com.pxinxs.todolistapp.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pxinxs.todolistapp.R
import com.pxinxs.todolistapp.databinding.FragmentTaskDetailsBinding
import com.pxinxs.todolistapp.domain.models.Task
import com.pxinxs.todolistapp.presentation.dialogs.DatePickerFragment
import com.pxinxs.todolistapp.presentation.disable
import com.pxinxs.todolistapp.presentation.enable
import com.pxinxs.todolistapp.presentation.utils.DateFormatter
import com.pxinxs.todolistapp.presentation.utils.viewmodel.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {

//    private val args: TaskDetailsFragment by navArgs()

    private val viewModel: TaskDetailsViewModel by viewModels()

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!

    private var datePickerFragment: DialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        binding.tvTitle.addTextChangedListener {
            viewModel.onTaskChanged(
                binding.tvTitle.text.toString(),
                binding.tvDescription.text.toString()
            )
        }
        binding.tvDescription.addTextChangedListener {
            viewModel.onTaskChanged(
                binding.tvTitle.text.toString(),
                binding.tvDescription.text.toString()
            )
        }
        binding.tvDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSave.setOnClickListener {
            viewModel.onSaveButtonClicked(
                title = binding.tvTitle.text.toString(),
                description = binding.tvDescription.text.toString()
            )
        }
        viewModel.taskId2.value = arguments?.getString(KEY_TASK_ID)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.task.collect { task ->
                    if (task == null) {
                        showCreateNewTask()
                    } else {
                        showTaskDetails(task)
                    }
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.saveButtonEnabled.collect { isEnabled ->
                binding.btnSave.isEnabled = isEnabled
            }
        }

        // Observe navigation events
        launchAndRepeatWithViewLifecycle {
            viewModel.navigationActions.collect { action ->
                if (action is TaskDetailsViewModel.TaskNavigationAction.CloseFragment) {
                    findNavController().navigate(R.id.action_taskDetailsFragment_to_taskListFragment)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        datePickerFragment?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        datePickerFragment?.dismissAllowingStateLoss()
        datePickerFragment = null
    }

    private fun showCreateNewTask() {
        val todayDateInMillis = viewModel.dateInMillis.value
        val todayDateFormatted = DateFormatter.getFormattedDate(todayDateInMillis)

        binding.tvTitle.enable()
        binding.tvDescription.enable()
        binding.tvDate.text = todayDateFormatted
        binding.btnSave.disable()
    }

    private fun showTaskDetails(task: Task) {
        binding.tvTitle.apply {
            setText(task.title)
            setBackgroundResource(android.R.color.transparent)
            disable()
        }
        binding.tvDescription.apply {
            setText(task.description)
            setBackgroundResource(android.R.color.transparent)
            disable()
        }
        binding.tvDate.text = DateFormatter.getFormattedDate(task.dateInMillis)
        binding.btnSave.enable()
    }

    private fun showDatePickerDialog() {
        datePickerFragment?.dismiss()
        datePickerFragment = DatePickerFragment().apply {
            setListener(dateSelectedListener)
        }.also { it.show(parentFragmentManager, null) }
    }

    private val dateSelectedListener = object : DatePickerFragment.DateListener {
        override fun onDateSelected(dateInMillis: Long) {
            viewModel.onDateSelected(dateInMillis)
        }
    }

    companion object {
        const val KEY_TASK_ID = "key_task_id"
    }
}