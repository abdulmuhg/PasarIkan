package id.co.personal.pasarikan.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.adapter.ItemAdapter
import id.co.personal.pasarikan.models.Item
import kotlinx.android.synthetic.main.activity_edit_item.*
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditItemActivity : AppCompatActivity() {
    private var itemId: String? = null
    private var database: FirebaseDatabase = Firebase.database
    private var dbRef: DatabaseReference
    private var imageUrl: String? = null
    private var user_id: String = ""
    private var item_ids: String = ""
    private var name: String = ""
    private var rating: Float = 0f
    private var address: String = ""
    private var min_buy: Int = 1
    private var item_images: String = ""
    private var seller_name: String = ""
    private var item_contact: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
        getExtraFromIntent()
        setOnClickButton()
    }

    init {
        dbRef = database.getReference("items")
    }

    private fun getExtraFromIntent() {
        itemId = intent?.getStringExtra(ItemAdapter.EXTRA_ITEM_ID)
        readItemDetail(itemId!!)
    }

    private fun readItemDetail(item_id: String) {
        val userListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val item = dataSnapshot.getValue<Item>()
                item?.let {
                    Glide.with(this@EditItemActivity)
                        .load(item.item_images)
                        .into(iv_details_images_e)
                    imageUrl = item.item_images
                    et_nama_barang.setText(item.name)
                    et_harga_barang.setText(item.price.toString())
                    et_stok.setText(item.stock.toString())
                    et_alamat.setText(item.address.toString())
                    et_deskripsi.setText(item.description)
                    if (et_deskripsi.text!!.isBlank()) {
                        et_deskripsi.setText("Tidak ada deskripsi")
                    }
                    user_id = item.user_id
                    item_ids = item.item_id
                    rating = item.rating
                    min_buy = item.min_buy
                    item_images = item.item_images
                    seller_name = item.seller_name
                    item_contact = item.contact
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child(item_id).addListenerForSingleValueEvent(userListener)
    }

    private fun saveData(
        name: String,
        price: String,
        stock: String,
        desc: String,
        alamat: String
    ) {
        val itemData = Item(
            user_id = user_id,
            item_id = item_ids,
            rating = rating,
            address = alamat,
            min_buy = min_buy,
            item_images = item_images,
            seller_name = seller_name,
            name = name,
            price = price.toInt(),
            stock = stock.toInt(),
            description = desc,
            contact = item_contact
        )
        dbRef.child(itemId.toString()).setValue(itemData).addOnSuccessListener {
            if (progress_save_item.visibility == View.VISIBLE) {
                progress_save_item.visibility = View.GONE
                finish()
            }
        }
            .addOnFailureListener {
                if (progress_save_item.visibility == View.VISIBLE) {
                    progress_save_item.visibility = View.GONE
                    btn_save.visibility = View.VISIBLE
                }
            }
    }

    private fun setOnClickButton() {
        btn_save.setOnClickListener {
            btn_save.visibility = View.GONE
            progress_save_item.visibility = View.VISIBLE
            saveData(
                et_nama_barang.text.toString(),
                et_harga_barang.text.toString(),
                et_stok.text.toString(),
                et_deskripsi.text.toString(),
                et_alamat.text.toString()
            )
        }
        toolbar_ubah_data.setNavigationOnClickListener {
            finish()
        }
    }
}