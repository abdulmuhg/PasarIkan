package id.co.personal.pasarikan

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth


object MyFunction {

    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

    fun getCurrentUser(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }

    fun myToast(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
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
}