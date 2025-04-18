package com.example.handmadeitem

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Get the user data from the Intent
        val username = intent.getStringExtra("username") ?: "N/A"
        val email = intent.getStringExtra("email") ?: "N/A"
        val firstName = intent.getStringExtra("firstName") ?: "N/A"
        val lastName = intent.getStringExtra("lastName") ?: "N/A"

        // Find the TextViews
        val txtUsername: TextView = findViewById(R.id.usernameEditText)
        val txtEmail: TextView = findViewById(R.id.emailEditText)
        val txtFirstName: TextView = findViewById(R.id.firstNameEditText)
        val txtLastName: TextView = findViewById(R.id.lastNameEditText)

        // Set the data in TextViews
        txtUsername.text = "$username"
        txtEmail.text = "$email"
        txtFirstName.text = "$firstName"
        txtLastName.text = "$lastName"

        txtUsername.isFocusable = false
        txtEmail.isFocusable = false
        txtUsername.isFocusableInTouchMode = false
        txtEmail.isFocusableInTouchMode = false

        val db = FirebaseFirestore.getInstance()


        val edtUsername: EditText = findViewById(R.id.usernameEditText)
        val edtEmail: EditText = findViewById(R.id.emailEditText) // Make this read-only
        val edtFirstName: EditText = findViewById(R.id.firstNameEditText)
        val edtLastName: EditText = findViewById(R.id.lastNameEditText)
        val edtBio: EditText = findViewById(R.id.bioEditText)
        val edtWebsite: EditText = findViewById(R.id.websiteEditText)
        val edtInterest: EditText = findViewById(R.id.interestEditText)
        val btnUpdate: Button = findViewById(R.id.saveButton)

        // Update button click listener
        btnUpdate.setOnClickListener {
            val newUsername = edtUsername.text.toString().trim()
            val newFirstName = edtFirstName.text.toString().trim()
            val newLastName = edtLastName.text.toString().trim()
            val newBio = edtBio.text.toString().trim()  // Bio (Optional)
            val newWebsite = edtWebsite.text.toString().trim()  // Website (Optional)
            val newInterest = edtInterest.text.toString().trim()

            if (newUsername.isEmpty() || newFirstName.isEmpty() || newLastName.isEmpty()) {
                Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use the username from Intent as the document ID
            val username1 = username.trim() // Use the username passed in the Intent

            val userDocRef = db.collection("users").document(username1)  // Document ID = username

            // Check if the document exists before updating
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userUpdates = mutableMapOf(
                            "firstName" to newFirstName,
                            "lastName" to newLastName
                        )
                        // Add bio and website if they are not empty
                        if (newBio.isNotEmpty()) userUpdates["bio"] = newBio
                        if (newWebsite.isNotEmpty()) userUpdates["website"] = newWebsite
                        if (newInterest.isNotEmpty()) userUpdates["interest"] = newInterest
                        // Update the Firestore document
                        userDocRef.update(userUpdates as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Profile Updated Successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Update Failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        // User not found in Firestore
                        Toast.makeText(this, "User not found in Firestore!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }


        }


        // Delete button click listener
        val btnDelete: Button = findViewById(R.id.deleteAccountButton)
        btnDelete.setOnClickListener {
            // Get the current authenticated user
            val auth = FirebaseAuth.getInstance()  // Firebase Authentication instance

            val currentUser = auth.currentUser




            // Check if the current user is authenticated
            if (currentUser != null) {
                val username1 = username.trim()

                // Delete user data from Firestore
                val userDocRef =
                    db.collection("users").document(username1)  // Document ID = username
                userDocRef.delete()
                    .addOnSuccessListener {
                        // Optionally, delete the user account from Firebase Authentication
                        currentUser.delete()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Account deleted successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Redirect user to a login screen or exit
                                finish()  // Close the activity
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Failed to delete account: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Failed to delete user data: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

