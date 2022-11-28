package com.example.inmygarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.inmygarden.databinding.ActivityPasswordResetBinding
import com.google.firebase.auth.FirebaseAuth

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPasswordResetBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.restPassBtn.setOnClickListener {
            val email: String = binding.emailReset.text.toString()
            if (!email.equals("")) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Password reset link is sent to your email",
                            Toast.LENGTH_SHORT
                        ).show()

                        // send user back to the login page
                        startActivity(Intent(this@PasswordResetActivity, LoginActivity::class.java))
                        finish()
                    }
                    else {
                            Toast.makeText(
                                applicationContext,
                                task.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }

            }
        }
    }
}