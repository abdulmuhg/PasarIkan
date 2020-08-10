package id.co.personal.pasarikan

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth


object MyFunction {

    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

    fun createLoadingDialog(context: Context, titleText: String = "Please Wait", cancelable: Boolean = false): SweetAlertDialog{
        val loadingProgress = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        loadingProgress.progressHelper.barColor = Color.parseColor("#64b5f6")
        loadingProgress.setCancelable(cancelable)
        loadingProgress.titleText = titleText
        return loadingProgress
    }
    fun createSuccessDialog(context: Context, titleText: String = "Success", contentText: String = "", confirmText: String = "OK"): SweetAlertDialog{
        val dialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = titleText
        dialog.contentText = contentText
        dialog.confirmText = confirmText
        return dialog
    }
    fun createErrorDialog(context: Context, titleText: String = "Error", contentText: String = "", confirmText: String = "OK"): SweetAlertDialog{
        val dialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        dialog.titleText = titleText
        dialog.contentText = contentText
        dialog.confirmText = confirmText
        return dialog
    }
    fun createWarningDialog(context: Context, titleText: String = "Are you sure?", contentText: String = "", confirmText: String = "OK"): SweetAlertDialog{
        val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = titleText
        dialog.contentText = contentText
        dialog.confirmText = confirmText
        return dialog
    }
    fun createDialog(context: Context, titleText: String = "Are you sure?", contentText: String = "", confirmText: String = "OK", cancelText: String = "NO"): SweetAlertDialog{
        val dialog = SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
        dialog.titleText = titleText
        dialog.contentText = contentText
        dialog.confirmText = confirmText
        dialog.cancelText = cancelText
        return dialog
    }
}