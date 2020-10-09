package com.bignerdranch.android.mobilecomputingproject
import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class DeleteDialogFragment : DialogFragment() {

    interface Callbacks {
        fun onPositiveDeleteClick()
        fun onNegativeDeleteClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle("Deleting task")
        ad.setMessage("Are you sure you want to delete this task?")

        ad.setPositiveButton("Yes" ,
            DialogInterface.OnClickListener { dialog, whichButton ->
                targetFragment?.let { fragment ->
                    (fragment as DeleteDialogFragment.Callbacks).onPositiveDeleteClick()
                }
            }
        )

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
