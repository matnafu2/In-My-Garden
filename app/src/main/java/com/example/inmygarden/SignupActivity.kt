package com.example.inmygarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.inmygarden.databinding.ActivityLoginBinding
import com.example.inmygarden.databinding.ActivitySingupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingupBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySingupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signup.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()

            if ( email != "" && password != "")
                signupWithFirebase(email, password)
            else
                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show()
        }
    }

    // signup the user using the firebase authentication service
    private fun signupWithFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Your account is created", Toast.LENGTH_SHORT).show()
                finish()  // finishes this activity and goes back to the activity that calls it
            }
            else { // error occurred while creating an account
                Toast.makeText(applicationContext, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}