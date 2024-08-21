package com.example.recipeappiti.auth.model.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

object AlertUtil {
    fun showAlert(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("Login Failed")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}