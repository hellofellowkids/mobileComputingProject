package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*

private const val TAG = "AddTaskFragment"

class AddTaskFragment : Fragment() {

    // Callbacks for fragment navigation
    interface Callbacks {
        fun onBackPressed()
    }

    private var callbacks: Callbacks? = null


    // widgets on screen
    private lateinit var prioritySelection : Spinner
    private lateinit var customBackButton : ImageView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate task_list view
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        // find widgets in layout
        prioritySelection = view.findViewById(R.id.spinner_priority) as Spinner
        customBackButton = view.findViewById(R.id.back_arrow_new_task) as ImageView

        // setup spinner
        setupSpinnerAdapter()

        return view
    }

    override fun onStart() {
        super.onStart()

        // <-- button
        customBackButton.setOnClickListener {
            // Log.d(TAG, "Back button pressed!")
            callbacks?.onBackPressed()
        }


        // priority selection
        prioritySelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "Selected item from spinner position = $position")
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun setupSpinnerAdapter() {
        // Array adapter for task filter
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.priority_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                prioritySelection.adapter = adapter
            }
        }
    }

    companion object {
        fun newInstance(): AddTaskFragment {
            return AddTaskFragment()
        }
    }
}