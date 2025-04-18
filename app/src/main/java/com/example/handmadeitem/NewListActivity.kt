package com.example.handmadeitem

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import java.util.*

class NewListActivity : AppCompatActivity() {

    private lateinit var selectedImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_list)

        FirebaseApp.initializeApp(this)  // Initialize Firebase
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val username = currentUser?.displayName ?: currentUser?.email ?: "username"  // Use displayName if set, otherwise email


        // ImageView Click Listeners for Photo Selection
        val image1: ImageView = findViewById(R.id.image1)
        val image2: ImageView = findViewById(R.id.image2)
        val image3: ImageView = findViewById(R.id.image3)

        val descriptionInput = findViewById<EditText>(R.id.descriptionInput)
        val priceInput = findViewById<EditText>(R.id.priceInput)
        val listItemButton = findViewById<Button>(R.id.listItemButton)

        image1.setOnClickListener { checkPermissionsAndShowOptions(image1) }
        image2.setOnClickListener { checkPermissionsAndShowOptions(image2) }
        image3.setOnClickListener { checkPermissionsAndShowOptions(image3) }

        listItemButton.setOnClickListener {
            val description = descriptionInput.text.toString()
            val price = priceInput.text.toString()

            // Convert images to URIs (Instead of converting Bitmap to Uri, we use the selected image's URI directly)
            val image1Uri = getImageUriFromImageView(image1)
            val image2Uri = getImageUriFromImageView(image2)
            val image3Uri = getImageUriFromImageView(image3)

            val uploadTasks = mutableListOf<Task<Uri>>()

// Add upload tasks if the URIs are not null
            image1Uri?.let { uri ->
                uploadTasks.add(uploadImageToStorage(uri))
            }
            image2Uri?.let { uri ->
                uploadTasks.add(uploadImageToStorage(uri))
            }
            image3Uri?.let { uri ->
                uploadTasks.add(uploadImageToStorage(uri))
            }

// After all uploads are complete, get the download URLs
            Tasks.whenAllSuccess<Uri>(uploadTasks).addOnSuccessListener { downloadUrls ->
                val image1Url = downloadUrls.getOrNull(0)?.toString() ?: ""
                val image2Url = downloadUrls.getOrNull(1)?.toString() ?: ""
                val image3Url = downloadUrls.getOrNull(2)?.toString() ?: ""

                // Proceed to store the data in Firestore
                val itemData = hashMapOf(
                    "image1" to image1Url,
                    "image2" to image2Url,
                    "image3" to image3Url,
                    "description" to description,
                    "price" to price,
                    "username" to username  // Store the username as a reference

                )

                db.collection("items")
                    .add(itemData)
                    .addOnSuccessListener {
                        // Item successfully listed, proceed with the next steps
                        Toast.makeText(this, "Item successfully listed", Toast.LENGTH_SHORT).show()
                        // Create an Intent to jump to ActivityDescription
                        val intent = Intent(this, ActivityDescription::class.java).apply {
                            // Add the necessary data to the intent
                            putExtra("image1", image1Url)
                            putExtra("image2", image2Url)
                            putExtra("image3", image3Url)
                            putExtra("item_description", description)
                            putExtra("item_price", price)
                        }
                        val listOfSellingIntent = Intent(this, ActivitySellingItem::class.java).apply {
                            putExtra("item_description", description)
                            putExtra("item_price", price)
                            putExtra("image1", image1Uri)
                            putExtra("image2", image2Uri)
                            putExtra("image3", image3Uri)
                        }

                        startActivity(listOfSellingIntent)
                        // Start the ActivityDescription activity
                        startActivity(intent)

                        // Finish the current activity (NewListActivity) so it doesn't remain in the back stack
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
    // Function to get the URI directly from the ImageView (no need to convert Bitmap)
    private fun getImageUriFromImageView(imageView: ImageView): Uri? {
        val drawable = imageView.drawable ?: return null
        val bitmap = (drawable as? BitmapDrawable)?.bitmap ?: return null

        // Create a ContentValues object to store metadata
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "tempImage_${UUID.randomUUID()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        // Insert the image into the MediaStore
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentResolver = contentResolver

        val imageUri = contentResolver.insert(contentUri, values)
        imageUri?.let {
            // Save the Bitmap to the newly created URI
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }

        return imageUri
    }


    // Function to upload image to Firebase Storage
    private fun uploadImageToStorage(imageUri: Uri): Task<Uri> {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("images/${UUID.randomUUID()}.jpg") // Unique file name

        // Upload image directly from URI
        return imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl // Return the download URL
            }



}



    // Function to check permissions and show image picker options
    private fun checkPermissionsAndShowOptions(imageView: ImageView) {
        selectedImageView = imageView

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_REQUEST_CODE
            )
        } else {
            showImagePickerOptions()
        }
    }

    // Function to show image picker options (camera or gallery)
    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()  // Open Camera
                1 -> openGallery()  // Open Gallery
            }
        }
        builder.show()
    }

    // Open Camera
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureImageLauncher.launch(intent)
    }

    // Open Gallery
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }


    // Handle Camera result
    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val bitmap: Bitmap? = data?.extras?.get("data") as Bitmap?
                bitmap?.let {
                    selectedImageView.setImageBitmap(it)
                } ?: run {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // Handle Gallery result
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                imageUri?.let {
                    selectedImageView.setImageURI(it)  // Display selected image
                } ?: run {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
