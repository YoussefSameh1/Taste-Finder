package com.example.TasteFinder.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.TasteFinder.BaseActivity
import com.example.TasteFinder.R
import com.example.TasteFinder.home.HomeActivity
import com.example.TasteFinder.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : BaseActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        nameEditText  = findViewById(R.id.register_name)
        emailEditText  = findViewById(R.id.register_email)
        passwordEditText = findViewById(R.id.register_password)
        confirmPasswordEditText  = findViewById(R.id.register_confirmPassword)
        registerButton = findViewById(R.id.registerBtn)
        val loginText: TextView = findViewById(R.id.register_login_text)

        firebaseAuth = FirebaseAuth.getInstance()

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        registerButton.setOnClickListener {
            registerUser(
                nameEditText.text.toString().trim(),
                emailEditText.text.toString().trim(),
                passwordEditText.text.toString().trim(),
                confirmPasswordEditText.text.toString().trim()
            )
        }


    }

    private fun registerUser(name: String, email: String, password: String, confirmPassword: String) {

        if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if(password == confirmPassword) {

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful) {
                        val userID = firebaseAuth.currentUser?.uid.toString()
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

                        val db = FirebaseFirestore.getInstance()
                        val userMap = hashMapOf(
                            "Name" to name,
                            "Email" to firebaseAuth.currentUser?.email,
                            "Favorites" to emptyList<String>()
                        )
                        db.collection("Users").document(userID).set(userMap).addOnSuccessListener {
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("userID", userID)
                            startActivity(intent)
                            finish()
                        }
                    }else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
