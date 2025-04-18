package com.example.handmadeitem

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        // Get the "Already Have an Account?" button
        val btnAlreadyHaveAccount: Button = findViewById(R.id.btnAlreadyHaveAccount)

        // Set an OnClickListener on the button to navigate to LoginActivity
        btnAlreadyHaveAccount.setOnClickListener {
            // Create an Intent to start LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Handling insets for edge-to-edge mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnJoinNow: Button = findViewById(R.id.btnJoinNow)

        btnJoinNow.setOnClickListener {
            // Create an Intent to start RegisterActivity
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }

        // Handling insets for edge-to-edge mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Get the "Go Free" TextView
        val btnGoFree: TextView = findViewById(R.id.txtGoFree)

// Set onClickListener for the "Go Free" text
        btnGoFree.setOnClickListener {
            // Handle the click event, for example, navigate to another activity
            val intent = Intent(this, ActivityHomePage::class.java)
            startActivity(intent)
        }

//        val storeIcon: ImageView = findViewById(R.id.storeIcon)
//        storeIcon.setOnClickListener {
//            val intent = Intent(this, StoreActivity::class.java)
//            startActivity(intent)
//        }

    }
}
