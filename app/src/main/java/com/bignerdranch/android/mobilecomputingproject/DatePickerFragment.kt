package com.bignerdranch.android.mobilecomputingproject

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"
private const val ARG_PROTOCOL = "protocol"

class DatePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onDateSelected(time: Date, protocol : Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Extract info from bundle
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val protocol = arguments?.getInt(ARG_PROTOCOL) as Int

        // setup callback
        val dateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->

            val resultDate : Date = GregorianCalendar(year, month, day).time

            targetFragment?.let { fragment ->
                (fragment as Callbacks).onDateSelected(resultDate, protocol)
            }
        }

        // setup calendar
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dp = DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )

        dp.datePicker.minDate = Date().time
        // calling from personal
        if(protocol == 2) {
            dp.datePicker.maxDate = date.time
        }

        return dp
    }

    companion object {
        fun newInstance(date: Date, protocol: Int): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
                putInt(ARG_PROTOCOL, protocol)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}