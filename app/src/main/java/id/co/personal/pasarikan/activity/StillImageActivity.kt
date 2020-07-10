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
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.classifier.ImageClassifier
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class StillImageActivity : BaseActivity() {

    private var currentPhotoFile: File? = null
    private var imagePreview: ImageView? = null
    private lateinit var classText: EditText
    private var classifier: ImageClassifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_still_image)
        imagePreview = findViewById(R.id.image_preview)
        classText = findViewById(R.id.result_text)

        // Setup image classifier.
        try {
            classifier = ImageClassifier(this)
        } catch (e: FirebaseMLException) {
            classText.setText(getString(R.string.fail_to_initialize_img_classifier))
        }

        buttonFunction()
        takePhoto()
    }

    private fun buttonFunction(){
        findViewById<ImageButton>(R.id.photo_camera_button)?.setOnClickListener { takePhoto() }
        findViewById<ImageButton>(R.id.photo_library_button)?.setOnClickListener { chooseFromLibrary() }
        findViewById<Button>(R.id.dummyButton)?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        classifier?.close()
        super.onDestroy()
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
                FirebaseVisionImage.fromFilePath(this, Uri.fromFile(currentPhotoFile)).also {
                    classifyImage(it.bitmap)
                }
            }
            REQUEST_PHOTO_LIBRARY -> {
                val selectedImageUri = data?.data ?: return
                FirebaseVisionImage.fromFilePath(this, selectedImageUri).also {
                    classifyImage(it.bitmap)
                }
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
                        "com.google.firebase.codelab.mlkit.automl.fileprovider",
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
        private const val TAG = "StillImageActivity"

        /** Request code for starting photo capture activity  */
        private const val REQUEST_IMAGE_CAPTURE = 1

        /** Request code for starting photo library activity  */
        private const val REQUEST_PHOTO_LIBRARY = 2

    }
}
