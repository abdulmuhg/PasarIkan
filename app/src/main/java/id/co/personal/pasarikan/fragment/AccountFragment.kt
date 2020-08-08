package id.co.personal.pasarikan.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import id.co.personal.pasarikan.MyFunction
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.activity.EditProfileActivity
import id.co.personal.pasarikan.activity.LoginRegisterActivity
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {
    private var storage: FirebaseStorage = Firebase.storage
    private var storageRef: StorageReference
    private var database: FirebaseDatabase = Firebase.database
    private var dbRef: DatabaseReference
    private var downloadUrl: String? = null
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var uid: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    init {
        dbRef = database.getReference("users")
        storageRef = storage.reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        currentUser?.let {
            uid = currentUser!!.uid
            readUserProfile(uid)
        }
        buttonFunction()
    }

    private fun readUserProfile(uid: String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                et_owner_name.setText(user!!.ownerName)
                et_email.setText(user.email)
                et_ktp.setText(user.noKTP)
                et_phone_number.setText(user.phoneNumber)
                et_shop_address.setText(user.address)
                downloadUrl = user.imageUrl
                Glide.with(this@AccountFragment)
                    .load(downloadUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_profile_picture)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child(uid).addListenerForSingleValueEvent(userListener)
    }

    private fun buttonFunction() {
        btn_goto_edit_profile.setOnClickListener {
            val i = Intent(context, EditProfileActivity::class.java)
            startActivity(i)
        }
        btn_logOut.setOnClickListener {
            val confirmDialog =
                MyFunction.createDialog(context!!, "Anda ingin Logout?", confirmText = "Ya", cancelText = "Batal")
            confirmDialog.setConfirmClickListener {
                auth.signOut().also {
                    val i = Intent(context, LoginRegisterActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(i)
                }
            }
            confirmDialog.setOnCancelListener {
                confirmDialog.dismissWithAnimation()
            }
            confirmDialog.show()
        }
    }
}