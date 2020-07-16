package id.co.personal.pasarikan.activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import id.co.personal.pasarikan.R

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance()
        Handler().postDelayed({
            val currentUser = auth.currentUser
            updateUI(currentUser)
            finish()
        }, SPLASH_TIME_OUT)
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val i = Intent(this, HomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
        } else {
            val i = Intent(this, LoginRegisterActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
        }
    }
}