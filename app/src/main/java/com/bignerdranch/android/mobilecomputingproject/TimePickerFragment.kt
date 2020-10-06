package com.bignerdranch.android.mobilecomputingproject

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalDateTime
import java.time.Month
import java.util.*

private const val ARG_Time = "time"
private const val ARG_PROTOCOL = "protocol"

class TimePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onTimeSelected(date: Date, protocol : Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Extract info from bundle
        val date = arguments?.getSerializable(ARG_Time) as Date
        val protocol = arguments?.getInt(ARG_PROTOCOL) as Int

        // setup callback
        val timeListener = TimePickerDialog.OnTimeSetListener {
                _: TimePicker, hour: Int, minute: Int ->

            val resultDate = date
            resultDate.hours = hour
            resultDate.minutes = minute
            resultDate.seconds = 0
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onTimeSelected(resultDate, protocol)
            }
        }

        // setup calendar
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), timeListener, hour, minute, false)
    }

    companion object {
        fun newInstance(date: Date, protocol: Int): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_Time, date)
                putInt(ARG_PROTOCOL, protocol)
            }

            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }
}