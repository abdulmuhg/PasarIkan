package id.co.personal.pasarikan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        buttonFunction()
        val database = Firebase.database
        dbRef = database.getReference("users")
        readUserProfile()

        storage = Firebase.storage
        // Get a non-default Storage bucket
        val storage = Firebase.storage("gs://pasar-ikan.appspot.com")

        // Create a storage reference from our app
        var storageRef = storage.reference
        // Create a child reference
        // imagesRef now points to "images"
        var imagesRef: StorageReference? = storageRef.child("images")

        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        var spaceRef = storageRef.child("images/space.jpg")
    }

    private fun buttonFunction() {
        btn_writeData.setOnClickListener {

            if (et_ktp.text.isNullOrBlank()) {
                et_ktp.error = "Nomor KTP tidak boleh kosong"
            } else {
                writeUserData(
                    "abdulmughni",
                    et_owner_name.text.toString(),
                    et_ktp.text.toString(),
                    et_shop_address.text.toString(),
                    et_phone_number.text.toString(),
                    et_email.text.toString()
                )
            }
        }
        toolbar_edit_profil.setNavigationOnClickListener {
            finish()
        }
    }

    private fun writeUserData(
        userName: String,
        ownerName: String,
        noKTP: String,
        address: String,
        phoneNumber: String,
        email: String
    ) {
        val userData = User(
            username = userName,
            ownerName = ownerName,
            noKTP = noKTP,
            address = address,
            phoneNumber = phoneNumber,
            email = email
        )
        dbRef.child("11").setValue(userData)
    }

    private fun readUserProfile() {
        Glide.with(this)
            .load(R.drawable.user_profile)
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile_picture)
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                et_owner_name.setText(user!!.ownerName)
                et_email.setText(user.email)
                et_ktp.setText(user.noKTP)
                et_phone_number.setText(user.phoneNumber)
                et_shop_address.setText(user.address)

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child("11").addValueEventListener(userListener)
    }
}