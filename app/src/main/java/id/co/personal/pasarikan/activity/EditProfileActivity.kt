package id.co.personal.pasarikan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        buttonFunction()

        val database = Firebase.database
        dbRef = database.getReference("users")
        readUserProfile()
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