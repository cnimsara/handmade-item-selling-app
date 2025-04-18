package com.example.handmadeitem

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide  // Import Glide

class ActivityDescription : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        val descriptionTextView = findViewById<TextView>(R.id.textView)
        val priceTextView = findViewById<TextView>(R.id.priceTextView)
        val imageMain = findViewById<ImageView>(R.id.imageView)
        val image1View = findViewById<ImageView>(R.id.image1View)
        val image2View = findViewById<ImageView>(R.id.image2View)
        val image3View = findViewById<ImageView>(R.id.image3View)

        // Get data from Intent
        val description = intent.getStringExtra("item_description")
        val price = intent.getStringExtra("item_price")
        val image1Uri = intent.getStringExtra("image1")
        val image2Uri = intent.getStringExtra("image2")
        val image3Uri = intent.getStringExtra("image3")

        // Set text
        descriptionTextView.text = description
        priceTextView.text = "Price: $price"

        // Use Glide to load images from URLs into ImageViews
        image1Uri?.let {
            Glide.with(this)
                .load(it)
                .into(imageMain)  // Set the first image as the main image
            Glide.with(this)
                .load(it)
                .into(image1View)  // Set the first image as an additional image
        }

        image2Uri?.let {
            Glide.with(this)
                .load(it)
                .into(image2View)
            image2View.visibility = View.VISIBLE
        }

        image3Uri?.let {
            Glide.with(this)
                .load(it)
                .into(image3View)
            image3View.visibility = View.VISIBLE
        }
    }
}
