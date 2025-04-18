package com.example.handmadeitem

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ActivityHomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")

        // Find the profile icon ImageView
        val profileIcon: ImageView = findViewById(R.id.userIcon)

        // Set an OnClickListener on the profile icon
        profileIcon.setOnClickListener {
            // Create an Intent to navigate to the Profile Page
            val Profileintent = Intent(this, ActivityProfile::class.java)

            // Pass the user data to the profile activity
            Profileintent.putExtra("username", username)
            Profileintent.putExtra("email", email)
            Profileintent.putExtra("firstName", firstName)
            Profileintent.putExtra("lastName", lastName)

            startActivity(Profileintent)

        }
            // Reference the image and text views after setContentView
            val featuredImage: ImageView = findViewById(R.id.featuredImage)
            val itemName: TextView = findViewById(R.id.itemName)
            val itemPrice: TextView = findViewById(R.id.itemPrice)

            // Set values dynamically
            featuredImage.setImageResource(R.drawable.strawhat)
            itemName.text = "Handmade Straw Hat"
            itemPrice.text = "$30.00"

            // Find the store icon ImageView
            val storeIcon: ImageView = findViewById(R.id.storeIcon)

            // Set an OnClickListener on the store icon
            storeIcon.setOnClickListener {
                // Create an Intent to navigate to the listing page
                val intent = Intent(this, ManageSellerActivity::class.java)
                startActivity(intent)
            }



        }
    }
