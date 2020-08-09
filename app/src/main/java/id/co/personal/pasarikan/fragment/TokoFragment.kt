package id.co.personal.pasarikan.fragment

import android.annotation.SuppressLint
import android.content.Intent
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
import id.co.personal.pasarikan.activity.EditProfileActivity
import id.co.personal.pasarikan.activity.ItemInputActivity
import id.co.personal.pasarikan.adapter.ItemAdapter
import id.co.personal.pasarikan.models.Item
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.fragment_toko.*

class TokoFragment : Fragment() {

    private lateinit var listItemItem: ArrayList<Item>
    private var database: FirebaseDatabase = Firebase.database
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var uid: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_toko, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listItemItem = ArrayList()
        listItemItem.clear()
        pb_list_item_d.visibility = View.VISIBLE
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        currentUser?.let {
            uid = currentUser!!.uid
            getItem(uid)
            readUserProfile(uid)
        }
        onClickButton()
    }

    private fun readUserProfile(uid: String) {
        dbRef = database.getReference("users")
        val userListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                user?.apply {
                    if (user.noKTP == "" || user.phoneNumber == "") {
                        container_lengkapi_data?.visibility = View.VISIBLE
                        main_container?.visibility = View.GONE
                        tv_empty?.visibility = View.GONE
                        btn_addItem?.isClickable = false
                        btn_addItem?.visibility = View.GONE
                    } else {
                        container_lengkapi_data?.visibility = View.GONE
                        main_container?.visibility = View.VISIBLE
                        btn_addItem?.isClickable = true
                        btn_addItem?.visibility = View.VISIBLE
                    }
                    tv_username?.text = "Halo, " + user.ownerName
                    context?.let {
                        Glide.with(it)
                            .load(user.imageUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(iv_profile_picture)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child(uid).addValueEventListener(userListener)
    }

    private fun getItem(uid: String) {
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pb_list_item_d.visibility = View.VISIBLE
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    if (items!!.user_id == uid) {
                        items.let { it1 -> listItemItem.add(it1) }
                    }
                }
                pb_list_item_d.visibility = View.INVISIBLE
                if (listItemItem.isEmpty()) {
                    //showEmpty()
                } else {
                    tv_empty.visibility = View.GONE
                    showRecyclerList()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addValueEventListener(itemListener)
    }

    private fun showRecyclerList() {
        tv_empty.visibility = View.GONE
        listItemItem.reverse()
        val adapter = ItemAdapter(context!!, listItemItem)
        rv_fish_item_d.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_fish_item_d.adapter = adapter
    }

    private fun onClickButton() {
        btn_addItem.setOnClickListener {
            startActivity(Intent(context, ItemInputActivity::class.java))
        }
        btn_empty_user_data?.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }
    }
}