package com.bignerdranch.android.mobilecomputingproject
import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class AlertDialogFragment : DialogFragment() {

    interface Callbacks {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle("Take a photo")
        ad.setMessage("Would you like to take a photo?")

        ad.setPositiveButton(R.string.yes ,
            DialogInterface.OnClickListener { dialog, whichButton ->
                targetFragment?.let { fragment ->
                    (fragment as AlertDialogFragment.Callbacks).onPositiveClick()
                }
            }
        )

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
