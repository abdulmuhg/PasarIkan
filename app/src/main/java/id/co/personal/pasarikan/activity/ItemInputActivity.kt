/**
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.co.personal.pasarikan.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import id.co.personal.pasarikan.MyFunction
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.classifier.ImageClassifier
import id.co.personal.pasarikan.models.Item
import id.co.personal.pasarikan.models.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_item_input.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class ItemInputActivity : BaseActivity() {

    private var currentPhotoFile: File? = null
    private var imagePreview: ImageView? = null
    private var classifier: ImageClassifier? = null
    private lateinit var classText: EditText
    private var dbRef: DatabaseReference
    private var storage: FirebaseStorage = Firebase.storage
    private var database: FirebaseDatabase = Firebase.database
    private var storageRef: StorageReference
    private var currentUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private var uid: String = ""
    private var item = Item()
    private var itemImageUrl: String? = null
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_input)
        imagePreview = findViewById(R.id.image_preview)
        classText = findViewById(R.id.result_text)
        auth = FirebaseAuth.getInstance()
        try {
            classifier = ImageClassifier(this)
        } catch (e: FirebaseMLException) {
            classText.setText(getString(R.string.fail_to_initialize_img_classifier))
        }
        buttonFunction()
        takePhoto()
    }

    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser
        currentUser?.let {
            uid = currentUser!!.uid
            readUserProfile(uid)
        }
    }

    init {
        database = Firebase.database
        storageRef = storage.reference
        dbRef = database.reference
    }

    private fun readUserProfile(uid: String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                item.user_id = user!!.userId
                item.address = user.address
                item.seller_name = user.ownerName
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child("users").child(uid).addListenerForSingleValueEvent(userListener)
    }

    private fun buttonFunction(){
        findViewById<ImageButton>(R.id.photo_camera_button)?.setOnClickListener { takePhoto() }
        findViewById<ImageButton>(R.id.photo_library_button)?.setOnClickListener { chooseFromLibrary() }
        findViewById<Button>(R.id.dummyButton)?.setOnClickListener {
            val isPriceEmpty = et_harga.text.toString().isEmpty()
            if (isPriceEmpty) {
                Toast.makeText(this, "Mohon cantumkan harga", Toast.LENGTH_SHORT).show()
            } else {
                item.item_id = System.currentTimeMillis().toString()
                item.name = classText.text.toString()
                item.description = ""
                item.price = et_harga.text.toString().toInt()
                if (imageUri == null) {
                    Toast.makeText(this, "Mohon Cantumkan Foto Barang", Toast.LENGTH_SHORT).show()
                } else {
                    uploadImage(imageUri)
                }
            }
        }
        toolbar_input_item.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        classifier?.close()
        super.onDestroy()
    }

    private fun uploadImage(uri: Uri?) {
        val loadingDialog = MyFunction.createLoadingDialog(this, "Uploading Image", true)
        loadingDialog.show()
        val ref: StorageReference = storageRef.child(
            "items/"+item.item_id+"/"+System.currentTimeMillis()
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
                itemImageUrl = task.result.toString()
                dbRef.child("items").child(item.item_id).child("item_images").setValue(itemImageUrl)
                writeItemData(
                    uid,
                    item.item_id,
                    item.name,
                    item.description,
                    item.price,
                    item.address,
                    itemImageUrl!!,
                    item.seller_name
                )
            } else {
                val errorDialog =
                    MyFunction.createErrorDialog(this, contentText = "Failed to upload an image")
                errorDialog.show()
            }
        }
    }

    private fun writeItemData(
        uid: String,
        item_id: String,
        name: String,
        desc: String,
        price: Int,
        address: String,
        images: String,
        sellerName: String
    ) {
        val itemData = Item(
            user_id = uid,
            item_id = item_id,
            name = name,
            description = desc,
            price = price,
            address = address,
            item_images = images,
            seller_name = sellerName
        )
        dbRef.child("items").child(item_id).setValue(itemData)
            .addOnSuccessListener {
                finish()
        }
            .addOnFailureListener {
                val errorDialog =
                    MyFunction.createErrorDialog(this, contentText = "Failed connect to server")
                errorDialog.show()
            }
    }

    /** Create a file to pass to camera app */
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = cacheDir
        return createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents.
            currentPhotoFile = this
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            // Make use of FirebaseVisionImage.fromFilePath to take into account
            // Exif Orientation of the image files.
            REQUEST_IMAGE_CAPTURE -> {
                val capturedImageUri = Uri.fromFile(currentPhotoFile)
                FirebaseVisionImage.fromFilePath(this, Uri.fromFile(currentPhotoFile)).also {
                    classifyImage(it.bitmap)
                }
                imageUri = capturedImageUri
            }
            REQUEST_PHOTO_LIBRARY -> {
                val selectedImageUri = data?.data ?: return
                FirebaseVisionImage.fromFilePath(this, selectedImageUri).also {
                    classifyImage(it.bitmap)
                }
                imageUri = selectedImageUri
            }
        }
    }

    /** Run image classification on the given [Bitmap] */
    private fun classifyImage(bitmap: Bitmap) {
        if (classifier == null) {
            classText.setText(getString(R.string.uninitialized_img_classifier_or_invalid_context))
            return
        }

        // Show image on screen.
        imagePreview?.setImageBitmap(bitmap)

        // Classify image.
        classifier?.classifyFrame(bitmap)?.
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    classText.setText("Ikan "+ task.result)
                } else {
                    val e = task.exception
                    Log.e(TAG, "Error classifying frame", e)
                    classText.setText(e?.message)
                }
            }
    }

    private fun chooseFromLibrary() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        // Only accept JPEG or PNG format.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, REQUEST_PHOTO_LIBRARY)
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent.
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go.
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: IOException) {
                    // Error occurred while creating the File.
                    Log.e(TAG, "Unable to save image to run classification.", e)
                    null
                }
                // Continue only if the File was successfully created.
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "id.co.personal.pasarikan.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    companion object {

        /** Tag for the [Log].  */
        private const val TAG = "ItemInputActivity"

        /** Request code for starting photo capture activity  */
        private const val REQUEST_IMAGE_CAPTURE = 1

        /** Request code for starting photo library activity  */
        private const val REQUEST_PHOTO_LIBRARY = 2

    }
}
