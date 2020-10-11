package com.bignerdranch.android.mobilecomputingproject
import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class AlertDialogFragment : DialogFragment() {

    //Will be implemented inside TaskFragment
    interface Callbacks {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    //Creating the dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle("Take a photo of task")
        ad.setMessage("Would you like to take a photo of the your hard work?")

        //Setting text for yes option and setting onPositiveClick to be execute when yes is selected
        ad.setPositiveButton(R.string.yes ,
            DialogInterface.OnClickListener { dialog, whichButton ->
                targetFragment?.let { fragment ->
                    (fragment as AlertDialogFragment.Callbacks).onPositiveClick()
                }
            }
        )

        //Setting text for no option and setting onNegativeClick to be execute when yes is selected
        ad.setNegativeButton(R.string.no ,
            DialogInterface.OnClickListener { dialog, whichButton ->
                targetFragment?.let { fragment ->
                    (fragment as AlertDialogFragment.Callbacks).onNegativeClick()
                }
            }
        )

        return ad.create();
    }

    companion object {
        fun newInstance(): AlertDialogFragment {
            return AlertDialogFragment()
        }
    }
}
