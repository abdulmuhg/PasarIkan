package id.co.personal.pasarikan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import id.co.personal.pasarikan.R
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {
    private var name: String? = null
    private var description: String? = null
    private var img: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        getExtraFromIntent()
        setData()
        setOnClickButton()
    }

    private fun getExtraFromIntent(){
        name = intent?.getStringExtra("fishName")
        description = intent?.getStringExtra("desc")
        img = intent?.getIntExtra("img", 0)
    }

    private fun setData(){
        tv_details_name.text = name
        tv_details_desc.text = description
        Glide.with(this)
            .load(getDrawable(img!!))
            .into(iv_details_images)
    }

    private fun setOnClickButton(){
        toolbar_item_details.setNavigationOnClickListener {
            finish()
        }
    }
}