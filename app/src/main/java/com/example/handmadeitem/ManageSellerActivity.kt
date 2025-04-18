package com.example.handmadeitem


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

class ManageSellerActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_seller)

        // Set up Edge-to-Edge for UI elements
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.soldItemsTitle)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle button click for listing a new item
        val listNewItemButton: TextView = findViewById(R.id.ListNewTitle)
        listNewItemButton.setOnClickListener {
            // Start the Add New Item Activity
            val intent = Intent(this, NewListActivity::class.java)
            startActivity(intent)
        }

        // Handle button click for listing a new item
        val sellingItemsButton: TextView = findViewById(R.id.sellingItemsTitle)
        sellingItemsButton.setOnClickListener {
            // Start the Add New Item Activity
            val intent = Intent(this, ActivitySellingItem::class.java)
            startActivity(intent)
        }
    }
}
