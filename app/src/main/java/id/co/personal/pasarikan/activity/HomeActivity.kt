package id.co.personal.pasarikan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.models.Fish
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.adapter.FishItemAdapter
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(){
    private lateinit var listFishItem: ArrayList<Fish>
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        readUserProfile()
        val fish = Fish().getListData()
        listFishItem = ArrayList()
        listFishItem.clear()
        listFishItem.addAll(fish!!)
        showRecyclerList()
        setOnClickButton()
        if (intent.hasExtra("data")){
            getDataFromIntent()
        }

    }
    private fun readUserProfile() {
        val database = Firebase.database
        dbRef = database.getReference("users")
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child("11").addValueEventListener(userListener)
    }
    private fun getDataFromIntent(){
        val ikan = intent?.getStringExtra("ikan")
        val harga = intent?.getStringExtra("harga")
        val newItem = Fish()

        val database = Firebase.database
        dbRef = database.getReference("users")
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                val city = user!!.address


            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child("11").addValueEventListener(userListener)

        newItem.name = ikan!!.toString()
        newItem.price = harga!!.toInt()
        newItem.city = "Bandung"
        newItem.description = ikan
        newItem.rating = 3f
        newItem.stock = 100
        newItem.photo = R.drawable.ic_round_image_120
        listFishItem.add(newItem)
        listFishItem.reverse()
    }

    private fun showRecyclerList(){
        val adapter = FishItemAdapter (this, listFishItem)
        rv_fish_item.layoutManager =LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_fish_item.adapter = adapter
    }

    private fun setOnClickButton(){
        btn_about.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, ItemInputActivity::class.java))
        }
    }
}