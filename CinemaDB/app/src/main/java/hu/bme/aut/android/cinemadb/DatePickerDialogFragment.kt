package hu.bme.aut.android.cinemadb

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import java.io.Serializable
import java.time.LocalDate

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = LocalDate.now()
        val datePickerFragment = DatePickerDialog(requireContext(), this, date.year, date.monthValue, date.dayOfMonth)
        datePickerFragment.datePicker.maxDate = System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 7L
        datePickerFragment.datePicker.minDate = System.currentTimeMillis()
        return datePickerFragment
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val result = DatePickerResult(year, month+1, dayOfMonth)
        findNavController().previousBackStackEntry?.savedStateHandle?.set("date", result)
    }

    data class DatePickerResult(
        val year: Int,
        val month: Int,
        val day: Int
    ): Serializable
}