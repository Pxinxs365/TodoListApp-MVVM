package com.pxinxs.todolistapp.presentation.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.lang.ref.WeakReference
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var callback: WeakReference<DateListener>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day).apply {
            datePicker.minDate = System.currentTimeMillis()
        }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)
        callback?.get()?.onDateSelected(calendar.timeInMillis)
    }

    fun setListener(dataListener: DateListener) {
        callback = WeakReference(dataListener)
    }

    interface DateListener {
        fun onDateSelected(dateInMillis: Long)
    }
}