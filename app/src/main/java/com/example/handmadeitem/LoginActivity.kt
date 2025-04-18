package com.example.handmadeitem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Find views
        val edtEmailOrUsername: EditText = findViewById(R.id.UsernameOrGmail)
        val edtPassword: EditText = findViewById(R.id.UserPsw)
        val btnLogin: Button = findViewById(R.id.btnLogIn)

        // Handle Login Button Click
        btnLogin.setOnClickListener {
            val emailOrUsername = edtEmailOrUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (emailOrUsername.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Check if the email or username exists in Firestore
                val userRef = db.collection("users")

                // First, check if it's a username
                userRef.whereEqualTo("username", emailOrUsername).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            // Found user by username, validate password
                            val user = documents.documents[0]
                            val storedPassword = user.getString("password")
                            if (storedPassword == password) {
                                // Password match, retrieve user data
                                val username = user.getString("username")
                                val email = user.getString("email")
                                val firstName = user.getString("firstName")
                                val lastName = user.getString("lastName")

                                // Create an Intent to navigate to the Home Page
                                val intent = Intent(this, ActivityHomePage::class.java)


                                // Pass user data to the home activity
                                intent.putExtra("username", username)
                                intent.putExtra("email", email)
                                intent.putExtra("firstName", firstName)
                                intent.putExtra("lastName", lastName)

                                // Start the home activity
                                startActivity(intent)


                                finish() // Close the login activity
                            } else {
                                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // If not found by username, check by email
                            userRef.whereEqualTo("email", emailOrUsername).get()
                                .addOnSuccessListener { emailDocuments ->
                                    if (!emailDocuments.isEmpty) {
                                        // Found user by email, validate password
                                        val emailUser = emailDocuments.documents[0]
                                        val storedPassword = emailUser.getString("password")
                                        if (storedPassword == password) {
                                            // Password match, retrieve user data
                                            val username = emailUser.getString("username")
                                            val email = emailUser.getString("email")
                                            val firstName = emailUser.getString("firstName")
                                            val lastName = emailUser.getString("lastName")

                                            // Create an Intent to navigate to the Home Page
                                            val intent = Intent(this, ActivityHomePage::class.java)

                                            // Pass user data to the home activity
                                            intent.putExtra("username", username)
                                            intent.putExtra("email", email)
                                            intent.putExtra("firstName", firstName)
                                            intent.putExtra("lastName", lastName)

                                            // Start the home activity
                                            startActivity(intent)

                                            finish() // Close the login activity
                                        } else {
                                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(this, "No user found with this username or email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
