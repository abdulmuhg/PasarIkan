package id.co.personal.pasarikan.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.activity.EditProfileActivity
import id.co.personal.pasarikan.activity.HomeActivity
import id.co.personal.pasarikan.activity.ItemInputActivity
import id.co.personal.pasarikan.adapter.ItemAdapter
import id.co.personal.pasarikan.models.Item
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var listItemItem: ArrayList<Item>
    private var database: FirebaseDatabase = Firebase.database
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        readUserProfile()
        listItemItem = ArrayList()
        listItemItem.clear()
        getItem()
        setOnClickButton()
        pb_list_item.visibility = View.VISIBLE
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

    private fun getItem(){
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pb_list_item.visibility = View.VISIBLE
//                val children = dataSnapshot.children
//                children.forEach {
//                    val items = it?.getValue<Item>()
//                    items?.let { it1 -> listItemItem.add(it1) }
//                    listItemItem.reverse()
//                }
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    items?.let { it1 -> listItemItem.add(it1) }
                }
                pb_list_item.visibility = View.INVISIBLE
                showRecyclerList()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addValueEventListener(itemListener)
    }

    private fun showRecyclerList(){
        listItemItem.reverse()
        val adapter = ItemAdapter (context!!, listItemItem)
        rv_fish_item.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_fish_item.adapter = adapter
    }

    private fun setOnClickButton(){
        btn_about.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
        floatingActionButton.setOnClickListener {
            startActivity(Intent(activity, ItemInputActivity::class.java))
        }
    }
}