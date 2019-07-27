package com.dani.nv.flickrsearcher.ui.widgets

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import com.dani.nv.flickrsearcher.R

class CustomDiag() {
    private var dialog: AlertDialog? = null

    fun showErrorDialog(ctx: Context, msg: String) {
        (ctx as Activity).runOnUiThread {
            val builder = AlertDialog.Builder(ctx)

            // Set the alert dialog title
            builder.setTitle(ctx.getString(R.string.diag_error_title))

            // Display a message on alert dialog
            builder.setMessage(msg)

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton(ctx.getString(R.string.yes)) { dialog, _ ->
                // Do something when user press the positive button
                dialog?.dismiss()
            }

            // Finally, make the alert dialog using builder
            dialog = builder.create()
            dialog?.show()
        }
    }
}

