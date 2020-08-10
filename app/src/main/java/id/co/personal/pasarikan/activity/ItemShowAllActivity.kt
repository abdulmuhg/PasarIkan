package id.co.personal.pasarikan.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.adapter.ItemAdapter
import id.co.personal.pasarikan.adapter.ItemOwnerAdapter
import id.co.personal.pasarikan.models.Item
import kotlinx.android.synthetic.main.activity_item_show_all.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_toko.*

class ItemShowAllActivity : AppCompatActivity() {

    private lateinit var listItemItem: ArrayList<Item>
    private var database: FirebaseDatabase = Firebase.database
    private var dbRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
    private var uid: String = ""
    private lateinit var auth: FirebaseAuth
    private var extra: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_show_all)
        auth = FirebaseAuth.getInstance()
        getUserId()
        getExtras()
        listItemItem = ArrayList()
        listItemItem.clear()
        onClickEvents()
        container_showEmpty.visibility = View.GONE
    }
    init {
        dbRef = database.getReference("users")
    }

    private fun getExtras(){
        extra = intent?.getStringExtra("extra").toString()
        when (extra) {
            "all" -> {
                getAllItem()
            }
            "loc" -> {
                val location = intent?.getStringExtra("request").toString()
                getItemLoc(location)
            }
            "owner" -> {
                getItemOwner()
            }
            "search" -> {
                val keyword = intent?.getStringExtra("request").toString()
                getSearchResult(keyword)
            }
        }
    }
    private fun getUserId() {
        currentUser = auth.currentUser
        currentUser?.let {
            uid = currentUser!!.uid
            readUserProfile(uid)
        }
    }

    private fun getAllItem() {
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    items?.let { it1 -> listItemItem.add(it1) }
                }
                showRecyclerList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addValueEventListener(itemListener)
    }

    private fun getItemOwner() {
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    if (items!!.user_id == uid) {
                        items.let { it1 -> listItemItem.add(it1) }
                    }
                }
                if (listItemItem.isEmpty()) {
                    showEmpty()
                } else {
                    showRecyclerListOwner()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addValueEventListener(itemListener)
    }

    private fun getItemLoc(loc: String?) {
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    if (items!!.address == loc) {
                        items.let { it1 -> listItemItem.add(it1) }
                    }
                }
                if (listItemItem.isEmpty()) {
                    showEmpty()
                } else {
                    showRecyclerList()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addValueEventListener(itemListener)
    }

    private fun getSearchResult(key: String?) {
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            @SuppressLint("DefaultLocale")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    if (
                        items!!.name.contains(key.toString())||
                            items.name.contains(key?.capitalize().toString())||
                            items.name.contains(key?.decapitalize().toString())||
                            items.name.contains(key?.toLowerCase().toString())||
                            items.seller_name.contains(key?.capitalize().toString())||
                            items.seller_name.contains(key?.decapitalize().toString())||
                            items.seller_name.contains(key?.toLowerCase().toString())||
                            items.description.contains(key?.capitalize().toString())||
                            items.description.contains(key?.decapitalize().toString())||
                            items.description.contains(key?.toLowerCase().toString())||
                            items.address.contains(key?.capitalize().toString())||
                            items.address.contains(key?.decapitalize().toString())||
                            items.address.contains(key?.toLowerCase().toString())
                            ) {
                        items.let { it1 -> listItemItem.add(it1) }
                    }
                }
                if (listItemItem.isEmpty()) {
                    showEmpty()
                } else {
                    showRecyclerList()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addValueEventListener(itemListener)
    }

    private fun showEmpty(){
        container_showEmpty?.visibility = View.VISIBLE
    }

    private fun showRecyclerList() {
        listItemItem.reverse()
            val adapter = ItemAdapter(this, listItemItem)
            rv_show_all.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_show_all.adapter = adapter

    }
    private fun showRecyclerListOwner() {
        listItemItem.reverse()
        val adapter = ItemOwnerAdapter(this, listItemItem)
        rv_show_all.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_show_all.adapter = adapter

    }

    private fun readUserProfile(uid: String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child(uid).addListenerForSingleValueEvent(userListener)
    }

    private fun onClickEvents(){
        toolbar_semua_barang.setNavigationOnClickListener {
            finish()
        }
    }
}