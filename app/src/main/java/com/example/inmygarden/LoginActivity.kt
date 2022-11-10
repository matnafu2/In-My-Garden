package com.example.inmygarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.inmygarden.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // get instance of the authentication service from firebase
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString();

            signinWithFireBase(email, password)
        }

        binding.singupBtn.setOnClickListener {
            // open the signup activity
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    // logs in the user if the user already created an account
    private fun signinWithFireBase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) { // sign in is successful
                Toast.makeText(applicationContext, "You are successfully logged in ", Toast.LENGTH_SHORT).show()

                // send the user to the home page once logged in
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else { // sign in failed
                Toast.makeText(applicationContext, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}