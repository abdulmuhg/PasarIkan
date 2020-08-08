package id.co.personal.pasarikan.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.adapter.ItemAdapter.Companion.EXTRA_ITEM_ID
import id.co.personal.pasarikan.models.Item
import kotlinx.android.synthetic.main.activity_item_detail.*
import java.text.NumberFormat
import java.util.*

class ItemDetailActivity : AppCompatActivity() {
    private var itemId: String? = null
    private var database: FirebaseDatabase = Firebase.database
    private var dbRef: DatabaseReference
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        getExtraFromIntent()
        setOnClickButton()
    }

    init {
        dbRef = database.getReference("items")
    }

    fun getRupiah(price: Int) {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        formatRupiah.format(price.toDouble())
    }

    private fun getExtraFromIntent() {
        itemId = intent?.getStringExtra(EXTRA_ITEM_ID)
        readItemDetail(itemId!!)
    }

    private fun readItemDetail(item_id: String) {
        val userListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val item = dataSnapshot.getValue<Item>()
                item?.let {
                    Glide.with(this@ItemDetailActivity)
                        .load(item.item_images)
                        .into(iv_details_images)
                    imageUrl = item.item_images
                    tv_price.text = "Rp" + item.price.toString()
                    tv_item_name.text = item.name
                    tv_stock.text = "Stok " + item.stock.toString() + " Kg"
                    tv_rating.text = item.rating.toString()
                    tv_seller_name.text = item.seller_name
                    tv_address.text = item.address
                    tv_desc.text = item.description
                    if (tv_desc.text.isBlank()) {
                        tv_desc.text = "Tidak ada deskripsi"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child(item_id).addListenerForSingleValueEvent(userListener)
    }

    private fun setOnClickButton() {
        toolbar_item_details.setNavigationOnClickListener {
            finish()
        }
        iv_details_images.setOnClickListener {
            val i = Intent(this, ItemImageDetailActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.putExtra(EXTRA_ITEM_IMAGE_URL, imageUrl)
            startActivity(i)
        }
    }

    companion object {
        const val EXTRA_ITEM_IMAGE_URL = "e_i_i_url"
    }
}