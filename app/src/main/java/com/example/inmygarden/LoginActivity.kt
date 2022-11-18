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

        binding.resetBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, PasswordResetActivity::class.java)
            startActivity(intent)
        }
    }

    // logs in the user if the user already created an account
    private fun signinWithFireBase(email: String, password: String) {

        // both email and password should be non-empty before using firebase authentication
        if (!email.equals("") && !password.equals("")) {
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
        else {
            Toast.makeText(applicationContext, "You have to provide both email and password", Toast.LENGTH_SHORT).show()
        }
    }

    // until users sign out, they can login without typing the username and password
    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser

        // user object will be null if the user has not logged in or singed out
        if (user != null) { // user already logged
            // send the user to home page without asking for user name and password
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}