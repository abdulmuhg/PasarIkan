package id.co.personal.pasarikan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.MyFunction
import id.co.personal.pasarikan.MyFunction.capitalizeWords
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.activity_email_password.*
import kotlinx.android.synthetic.main.activity_email_password.et_email
import java.util.regex.Pattern

class EmailPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var signInState: Boolean = true
    private var database: FirebaseDatabase = Firebase.database
    private var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)
        auth = FirebaseAuth.getInstance()
        onClickEvents()
    }
    init {
        dbRef = database.getReference("users")
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {

        }
    }

    private fun onClickEvents() {
        switch_state.setOnClickListener {
            if (signInState) {
                container_signIn.visibility = View.GONE
                container_signUp.visibility = View.VISIBLE
                text_signUp.visibility = View.GONE
                text_signIn.visibility = View.VISIBLE
            } else {
                container_signIn.visibility = View.VISIBLE
                container_signUp.visibility = View.GONE
                text_signUp.visibility = View.VISIBLE
                text_signIn.visibility = View.GONE
            }
        }
        btn_signIn.setOnClickListener {
            signIn(et_email.text.toString(), et_password.text.toString())
        }
        btn_signUp.setOnClickListener {
            val email = et_email_reg.text.toString().trim()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (et_password_reg.text.toString() == et_password_reg_confirm.text.toString()) {
                    signUpNewUser(et_email_reg.text.toString(), et_password_reg.text.toString())
                } else {
                    layout_password_reg.error = "Password dan konfirmasi password tidak sesuai"
                    layout_password_reg_confirm.error =
                        "Password dan konfirmasi password tidak sesuai"
                }
            } else {
                layout_email_reg.error = "Email tidak valid"
            }
        }
    }

    private fun signUpNewUser(email: String, password: String) {
        val loading = MyFunction.createLoadingDialog(this)
        loading.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val successDialog =
                        MyFunction.createSuccessDialog(this, "Success", "Pendaftaran Berhasil")
                    successDialog.show()
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val userName = getDisplayName(et_email_reg.text.toString())
                    val displayName = userName.capitalizeWords()
                    user?.let {
                        val userEmail = user.email
                        val uid = user.uid
                        writeUserData(
                            uid = uid,
                            imageUrl = "",
                            email = userEmail!!,
                            phoneNumber = "",
                            address = "",
                            noKTP = "",
                            ownerName = displayName,
                            userName = userName
                        )
                    }
                    successDialog.setConfirmClickListener {
                        updateUI(user)
                    }
                } else {
                    loading.dismissWithAnimation()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        val loading = MyFunction.createLoadingDialog(this)
        loading.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loading.dismissWithAnimation()
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    loading.dismissWithAnimation()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                    // ...
                }

                // ...
            }
    }

    private fun writeUserData(
        uid: String,
        userName: String,
        ownerName: String,
        noKTP: String,
        address: String,
        phoneNumber: String,
        email: String,
        imageUrl: String
    ) {
        val userData = User(
            username = userName,
            ownerName = ownerName,
            noKTP = noKTP,
            address = address,
            phoneNumber = phoneNumber,
            email = email,
            imageUrl = imageUrl
        )
        dbRef.child(uid).setValue(userData)
            .addOnSuccessListener {

        }
            .addOnFailureListener {

            }
    }

    private fun getDisplayName(displayName: String): String{
        val startIndex = displayName.indexOf("@")
        val lastIndex = displayName.lastIndex
        return displayName.removeRange(startIndex, lastIndex+1)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}