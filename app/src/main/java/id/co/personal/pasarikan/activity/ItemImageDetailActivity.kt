package id.co.personal.pasarikan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.activity.ItemDetailActivity.Companion.EXTRA_ITEM_IMAGE_URL
import kotlinx.android.synthetic.main.activity_item_image_detail.*

class ItemImageDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_image_detail)
        onClickEvents()

        val url = intent?.getStringExtra(EXTRA_ITEM_IMAGE_URL)
        Glide.with(this)
            .load(url)
            .into(iv_item_image_detail)
    }

    private fun onClickEvents() {
        toolbar_item_image_detail.setNavigationOnClickListener {
            finish()
        }
    }
}