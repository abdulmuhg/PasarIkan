package id.co.personal.pasarikan.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private var downloadUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        buttonFunction()
        val database = Firebase.database
        dbRef = database.getReference("users")
        readUserProfile()
        storage = Firebase.storage
        storageRef = storage.reference

    }

    private fun uploadImage(uri: Uri?) {
        val loadingProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loadingProgress.progressHelper.barColor = Color.parseColor(R.color.primaryColor.toString())
        loadingProgress.setCancelable(true)
        loadingProgress.show()
        val ref: StorageReference = storageRef.child(
            "images/11/profile_picture"
        )
        val uploadTask = ref.putFile(uri!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                loadingProgress.dismissWithAnimation()
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadingProgress.dismissWithAnimation()
                downloadUrl = task.result.toString()
                Toast.makeText(this, "Image Upload Successful", Toast.LENGTH_SHORT).show()
            } else {
                // Handle failures
                // ...
            }
        }
    }

    private fun buttonFunction() {
        btn_writeData.setOnClickListener {
            if (et_ktp.text.isNullOrBlank()) {
                et_ktp.error = "Nomor KTP tidak boleh kosong"
            } else {
                uploadImage(imageUri)
                writeUserData(
                    "abdulmughni",
                    et_owner_name.text.toString(),
                    et_ktp.text.toString(),
                    et_shop_address.text.toString(),
                    et_phone_number.text.toString(),
                    et_email.text.toString(),
                    downloadUrl!!
                )
            }

        }
        toolbar_edit_profil.setNavigationOnClickListener {
            finish()
        }
        btn_uploadImage.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            imageUri = data.data
            Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile_picture)
        }
    }

    private fun writeUserData(
        userName: String,
        ownerName: String,
        noKTP: String,
        address: String,
        phoneNumber: String,
        email: String,
        imageUrl: String
    ) {
        val userData = User(
            username = userName,
            ownerName = ownerName,
            noKTP = noKTP,
            address = address,
            phoneNumber = phoneNumber,
            email = email,
            imageUrl = imageUrl
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
                downloadUrl = user.imageUrl
                Glide.with(this@EditProfileActivity)
                    .load(downloadUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_profile_picture)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child("11").addValueEventListener(userListener)
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val PICK_IMAGE_REQUEST = 2
    }
}