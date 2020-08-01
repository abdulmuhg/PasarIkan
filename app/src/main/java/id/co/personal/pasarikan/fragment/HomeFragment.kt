package id.co.personal.pasarikan.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.adapter.ItemAdapter
import id.co.personal.pasarikan.models.Item
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var listItemItem: ArrayList<Item>
    private var database: FirebaseDatabase = Firebase.database
    private lateinit var dbRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
    private var uid: String = ""
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    init {
        dbRef = database.getReference("users")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        getUserId()
        listItemItem = ArrayList()
        listItemItem.clear()
        getItem()
    }
    private fun getUserId(){
        currentUser = auth.currentUser
        currentUser?.let {
            uid = currentUser!!.uid
            readUserProfile(uid)
        }
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

    private fun getItem(){
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    items?.let { it1 -> listItemItem.add(it1) }
                }
                //showRecyclerList()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addValueEventListener(itemListener)
    }
}