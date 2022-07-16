package com.pxinxs.todolistapp.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pxinxs.todolistapp.R
import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.data.repositories.TasksRepository
import com.pxinxs.todolistapp.databinding.FragmentTaskDetailsBinding
import com.pxinxs.todolistapp.presentation.dialogs.DatePickerFragment
import com.pxinxs.todolistapp.presentation.disable
import com.pxinxs.todolistapp.presentation.enable
import com.pxinxs.todolistapp.presentation.utils.dateprovider.DateProvider
import com.pxinxs.todolistapp.presentation.utils.dateprovider.IDateProvider
import com.pxinxs.todolistapp.presentation.utils.uuidgenerator.IUuidGenerator
import com.pxinxs.todolistapp.presentation.utils.uuidgenerator.UuidGenerator

class TaskDetailsFragment : Fragment(), TaskDetailsView, TaskDetailsRouter {

    // should be replaced with DI, made for simplicity
    private val uuidGenerator: IUuidGenerator by lazy { UuidGenerator() }
    private val dateProvider: IDateProvider by lazy { DateProvider() }
    private val tasksRepository: ITasksRepository by lazy { TasksRepository.getInstance() }
    private val presenter by lazy {
        TaskDetailsPresenter(
            dateProvider,
            uuidGenerator,
            tasksRepository
        )
    }

    private var datePickerFragment: DialogFragment? = null

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        binding.tvTitle.addTextChangedListener {
            presenter.onTaskChanged(
                binding.tvTitle.text.toString(),
                binding.tvDescription.text.toString()
            )
        }
        binding.tvDescription.addTextChangedListener {
            presenter.onTaskChanged(
                binding.tvTitle.text.toString(),
                binding.tvDescription.text.toString()
            )
        }
        binding.tvDate.setOnClickListener { presenter.onDateClicked() }
        binding.btnSave.setOnClickListener {
            presenter.onSaveButtonClicked(
                title = binding.tvTitle.text.toString(),
                description = binding.tvDescription.text.toString()
            )
        }

        presenter.setTaskId(arguments?.getString(KEY_TASK_ID))
        presenter.onTakeView(this)
        presenter.onTakeRouter(this)
        savedInstanceState?.getString(KEY_DATE_VALUE)?.let { restoredDate ->
            presenter.restoreDate(restoredDate)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        datePickerFragment?.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_DATE_VALUE, binding.tvDate.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        datePickerFragment?.dismissAllowingStateLoss()
        datePickerFragment = null
        presenter.onDropView()
        presenter.onDropRouter()
    }

    override fun showCreateNewTask(defaultDateFormatted: String) {
        binding.tvTitle.enable()
        binding.tvDescription.enable()
        binding.tvDate.text = defaultDateFormatted
        binding.btnSave.disable()
    }

    override fun showTaskDetails(title: String, description: String, formattedDate: String) {
        binding.tvTitle.apply {
            setText(title)
            setBackgroundResource(android.R.color.transparent)
            disable()
        }
        binding.tvDescription.apply {
            setText(description)
            setBackgroundResource(android.R.color.transparent)
            disable()
        }
        binding.tvDate.text = formattedDate
        binding.btnSave.enable()
    }

    override fun showDatePickerDialog() {
        datePickerFragment?.dismiss()
        datePickerFragment = DatePickerFragment().apply {
            setListener(dateSelectedListener)
        }.also { it.show(parentFragmentManager, null) }
    }

    private val dateSelectedListener = object : DatePickerFragment.DateListener {
        override fun onDateSelected(dateInMillis: Long) {
            presenter.onDateSelected(dateInMillis)
        }
    }

    override fun showDate(formattedDate: String) {
        binding.tvDate.text = formattedDate
    }

    override fun showSaveButtonEnabled() {
        binding.btnSave.enable()
    }

    override fun showSaveButtonDisabled() {
        binding.btnSave.disable()
    }

    override fun closeScreen() {
        findNavController().navigate(R.id.action_taskDetailsFragment_to_taskListFragment)
    }

    companion object {
        const val KEY_TASK_ID = "key_task_id"
        private const val KEY_DATE_VALUE = "key_date_value"
    }
}