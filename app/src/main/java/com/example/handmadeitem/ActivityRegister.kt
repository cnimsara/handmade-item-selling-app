package com.example.handmadeitem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore



class ActivityRegister : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = FirebaseFirestore.getInstance()
        // Get all the form fields
        val edtFirstName: EditText = findViewById(R.id.firstName)
        val edtLastName: EditText = findViewById(R.id.lastName)
        val edtUsername: EditText = findViewById(R.id.username)
        val edtEmail: EditText = findViewById(R.id.email)
        val edtPassword: EditText = findViewById(R.id.password)
        val edtConfirmPassword: EditText = findViewById(R.id.confirmPassword)
        val chkTerms: CheckBox = findViewById(R.id.agreeTerms)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnGoToLogin: TextView = findViewById(R.id.login_text)

        // Set listener for the register button
        btnRegister.setOnClickListener {
            // Check if all fields are filled
            val firstName = edtFirstName.text.toString().trim()
            val lastName = edtLastName.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()


            // Validate input fields
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (!chkTerms.isChecked) {
                Toast.makeText(
                    this,
                    "You must agree to the terms and conditions",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                db.collection("users").document(username).get()
                    .addOnSuccessListener { document ->
                        // Create user data
                        if (document.exists()) {
                            // If the username exists, show an error message
                            Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            // If the username does not exist, proceed with the registration
                            val user = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "username" to username,
                                "email" to email,
                                "password" to password // ⚠️ Encrypt this before storing in production
                            )

                            // Store user data in Firestore using the username as the document ID
                            db.collection("users")
                                .document(username)  // Use username as the document ID
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Registration Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }
                    }
            }

            // Set listener for the "Go to Login" button
            btnGoToLogin.setOnClickListener {
                // Redirect to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Close RegisterActivity
            }
        }
    }
    }
