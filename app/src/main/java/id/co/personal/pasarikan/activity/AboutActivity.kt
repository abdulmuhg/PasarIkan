package id.co.personal.pasarikan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.co.personal.pasarikan.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        Glide.with(this)
            .load(R.drawable.user_profile)
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)
    }
}