package com.bignerdranch.android.mobilecomputingproject

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.util.*

class SchedulerFragment : Fragment() {
    lateinit var personaldate: Button
    private lateinit var realdate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        personaldate = view.findViewById(R.id.personal_deadline)
        realdate = view.findViewById(R.id.real_deadline)
        realdate.setOnClickListener {



        }
return view
    }
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(MainActivity().context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

        // Display Selected date in textbox
        realdate.setText("" + dayOfMonth + " " + monthOfYear + ", " + year)

    }, year, month, day)

}