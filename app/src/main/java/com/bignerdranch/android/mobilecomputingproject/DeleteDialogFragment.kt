package com.bignerdranch.android.mobilecomputingproject
import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class DeleteDialogFragment : DialogFragment() {

    //Will be implemented inside TaskFragment
    interface Callbacks {
        fun onPositiveDeleteClick()
        fun onNegativeDeleteClick()
    }

    //Create dialog asking if the user is sure of deleting the desired task
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle("Deleting task")
        ad.setMessage("Are you sure you want to delete this task?")

        //If yes call onPositiveDeleteClick
        ad.setPositiveButton("Yes" ,
            DialogInterface.OnClickListener { dialog, whichButton ->
                targetFragment?.let { fragment ->
                    (fragment as DeleteDialogFragment.Callbacks).onPositiveDeleteClick()
                }
            }
        )

        //If no call onNegativeDeleteClick
        ad.setNegativeButton("No" ,
            DialogInterface.OnClickListener { dialog, whichButton ->
                targetFragment?.let { fragment ->
                    (fragment as DeleteDialogFragment.Callbacks).onNegativeDeleteClick()
                }
            }
        )

        return ad.create();
    }

    companion object {
        fun newInstance(): DeleteDialogFragment {
            return DeleteDialogFragment()
        }
    }
}
