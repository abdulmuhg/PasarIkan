package id.co.personal.pasarikan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import id.co.personal.pasarikan.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val splashTimeOut: Long = 3000
    private var currentUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setBackgroundDrawable(null)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            auth = FirebaseAuth.getInstance()
            currentUser = auth.currentUser
            updateUI(currentUser)
        }, splashTimeOut)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else {
            val i = Intent(this, LoginRegisterActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }
    }
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}