package id.co.personal.pasarikan.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.activity.ItemInputActivity
import id.co.personal.pasarikan.adapter.ItemAdapter
import id.co.personal.pasarikan.models.Item
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var listItemItem: ArrayList<Item>
    private var database: FirebaseDatabase = Firebase.database
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        readUserProfile()
        listItemItem = ArrayList()
        listItemItem.clear()
        getItem()
        pb_list_item_d.visibility = View.VISIBLE
        onClickButton()
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
                pb_list_item_d.visibility = View.VISIBLE
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    items?.let { it1 -> listItemItem.add(it1) }
                }
                pb_list_item_d.visibility = View.INVISIBLE
                if (listItemItem.isEmpty()) {
                    showEmpty()
                }
                else {
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
        tv_empty.visibility = View.VISIBLE
    }

    private fun showRecyclerList(){
        tv_empty.visibility = View.GONE
        listItemItem.reverse()
        val adapter = ItemAdapter (context!!, listItemItem)
        rv_fish_item_d.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_fish_item_d.adapter = adapter
    }

    private fun onClickButton(){
        btn_addItem.setOnClickListener {
            startActivity(Intent(context, ItemInputActivity::class.java))
        }
    }
}