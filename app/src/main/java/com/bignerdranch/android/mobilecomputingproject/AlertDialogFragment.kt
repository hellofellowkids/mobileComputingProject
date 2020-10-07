package com.bignerdranch.android.mobilecomputingproject
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*




class AlertDialogFragment : DialogFragment() {

    interface Callbacks {
        fun onDateSelected(time: Date, protocol : Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Extract info from bundle

        // setup callback

        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle("Take a photo")
        ad.setMessage("Would you like to take a photo?")




        val positiveListener = DialogInterface.OnClickListener()  {
            fun onClick(dialog : Dialog, which : Int) {

            }
        }

        val negativeListener = DialogInterface.OnClickListener() {
            fun onClick(dialog : Dialog, which : Int) {
                // continue with option
            }
        }
        ad.setPositiveButton(android.R.string.yes, positiveListener)
        ad.setNegativeButton(android.R.string.no,negativeListener)



        return ad;
    }

    companion object {
        fun newInstance(): AlertDialogFragment {

            return AlertDialogFragment()
        }
    }
}


