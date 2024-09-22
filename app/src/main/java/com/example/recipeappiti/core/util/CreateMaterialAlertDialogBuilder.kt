package com.example.recipeappiti.core.util

import android.content.Context
import com.example.recipeappiti.core.model.remote.FailureReason
import com.example.recipeappiti.core.model.remote.Response
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

    fun createFailureResponse(response: Response.Failure, context: Context, action: (() -> Unit)? = null) {
        when (val failureReason = response.reason) {
            is FailureReason.NoInternet -> {
                createMaterialAlertDialogBuilderOkCancel(
                    context,
                    title = "No Internet Connection",
                    message = "Please check your internet connection and try again.",
                    positiveBtnMsg = "Try again",
                    negativeBtnMsg = "Cancel"
                ) {
                    action?.invoke()
                }
            }

            is FailureReason.UnknownError -> {
                val errorMessage = failureReason.error
                createMaterialAlertDialogBuilderOk(
                    context,
                    title = "Unknown Error",
                    message = "An unknown error occurred: $errorMessage",
                    positiveBtnMsg = "Try again"
                ) {
                    action?.invoke()
                }
            }
        }
    }
}