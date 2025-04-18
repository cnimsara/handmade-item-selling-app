package com.example.handmadeitem

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.bumptech.glide.Glide // Optional if you want to load images

class ActivitySellingItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selling_item)

        // Set up WindowInsets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.SellingItem)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Fetch the item details from Firestore (for example, "item1" document in "items" collection)
        val db = FirebaseFirestore.getInstance()

        // Get the itemId (you can pass this via Intent or use a fixed item ID)
        val itemId = intent.getStringExtra("Item_Id") ?: "defaultItemId" // Replace with actual item ID passed

        // Fetch the document from Firestore based on item ID
        db.collection("items").document(itemId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Item data found, extract it and display in the UI
                    val description = document.getString("description") ?: "No description"
                    val price = document.getString("price") ?: "No price"
                    val image1Url = document.getString("image1") ?: ""
                    val image2Url = document.getString("image2") ?: ""
                    val image3Url = document.getString("image3") ?: ""

                    // Populate the views with the fetched data
                    val descriptionTextView = findViewById<TextView>(R.id.textView)
                    val priceTextView = findViewById<TextView>(R.id.priceTextView)
                    val imageMain = findViewById<ImageView>(R.id.imageView)
                    val image1View = findViewById<ImageView>(R.id.image1View)
                    val image2View = findViewById<ImageView>(R.id.image2View)
                    val image3View = findViewById<ImageView>(R.id.image3View)

                    descriptionTextView.text = description
                    priceTextView.text = "Price: $price"

                    // Load the images using Glide (or any other image loading library)
                    Glide.with(this).load(image1Url).into(imageMain)
                    if (!image2Url.isNullOrEmpty()) {
                        Glide.with(this).load(image2Url).into(image2View)
                        image2View.visibility = ImageView.VISIBLE
                    }
                    if (!image3Url.isNullOrEmpty()) {
                        Glide.with(this).load(image3Url).into(image3View)
                        image3View.visibility = ImageView.VISIBLE
                    }

                } else {
                    Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching item: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
