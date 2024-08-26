package com.example.recipeappiti.core.model.util

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object CreateMaterialAlertDialogBuilder {

    fun createMaterialAlertDialogBuilderOkCancel(
        context: Context,
        title: String,
        message: String,
        positiveBtnMsg: String,
        negativeBtnMsg: String,
        positiveBtnFun: () -> Unit
    ) {

        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveBtnMsg) { dialog, _ ->

                positiveBtnFun()

                dialog.dismiss()
            }
            .setNegativeButton(negativeBtnMsg) { dialog, _ ->

                dialog.dismiss()
            }
            .setCancelable(false)
            .show()

    }

    fun createMaterialAlertDialogBuilderOk(
        context: Context,
        title: String,
        message: String,
        positiveBtnMsg: String,
        positiveBtnFun: () -> Unit
    ) {

        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveBtnMsg) { dialog, _ ->

                positiveBtnFun()

                dialog.dismiss()
            }
            .setCancelable(false)
            .show()

    }

}