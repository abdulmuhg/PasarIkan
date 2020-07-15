package id.co.personal.pasarikan.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import id.co.personal.pasarikan.MyFunction
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    private var storage: FirebaseStorage = Firebase.storage
    private var storageRef: StorageReference
    private var database: FirebaseDatabase = Firebase.database
    private var dbRef: DatabaseReference
    private var imageUri: Uri? = null
    private var downloadUrl: String? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        auth = FirebaseAuth.getInstance()
        readUserProfile()
        buttonFunction()
    }

    init {
        dbRef = database.getReference("users")
        storageRef = storage.reference
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

    private fun buttonFunction() {
        btn_writeData.setOnClickListener {
            if (et_ktp.text.isNullOrBlank()) {
                et_ktp.error = "Nomor KTP tidak boleh kosong"
            } else {
                btn_writeData.visibility = View.GONE
                progress_write_data.visibility = View.VISIBLE
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
        btn_logOut.setOnClickListener {
            auth.signOut().also {
                startActivity(Intent(this, EmailPasswordActivity::class.java))
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImage(uri: Uri?) {
        val loadingDialog = MyFunction.createLoadingDialog(this)
        loadingDialog.show()
        val ref: StorageReference = storageRef.child(
            "images/11/profile_picture"
        )
        val uploadTask = ref.putFile(uri!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                loadingDialog.dismissWithAnimation()
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadingDialog.dismissWithAnimation()
                downloadUrl = task.result.toString()
                dbRef.child("11").child("imageUrl").setValue(downloadUrl)
                val successDialog = MyFunction.createSuccessDialog(
                    context = this,
                    titleText = "Success",
                    contentText = "Image has been uploaded"
                )
                successDialog.show()
            } else {
                val errorDialog =
                    MyFunction.createErrorDialog(this, contentText = "Failed to upload an image")
                errorDialog.show()
            }
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
        dbRef.child("11").setValue(userData).addOnSuccessListener {
            if (progress_write_data.visibility == View.VISIBLE) {
                progress_write_data.visibility = View.GONE
            }
            finish()
        }
            .addOnFailureListener {
                if (progress_write_data.visibility == View.VISIBLE) {
                    progress_write_data.visibility = View.GONE
                    btn_writeData.visibility = View.VISIBLE
                }
            }
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
            uploadImage(imageUri)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 2
    }
}