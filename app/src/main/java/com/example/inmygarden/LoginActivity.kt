package com.example.inmygarden

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.inmygarden.databinding.ActivityLoginBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import java.util.*

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

        // this is a test for the notification
        handleNotification()
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

    //--------------------------------------------------------------------------//
    //--------------------------------------------------------------------------//
    private fun handleNotification() {

        val calendar  = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // on a click of a button show a time picker
        binding.notification.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(currentHour)
                .setMinute(currentMinute)
                .setTitleText("Set Notification Time")
                .build()

            // get the selected time from the time picker
            timePicker.show(supportFragmentManager, "1")
            timePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)

                val intent = Intent(applicationContext, NotificationReceiver::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(
                        applicationContext, 100,
                        intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                // the pending intent will be called once a day
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY
                    ,pendingIntent)

                Toast.makeText(applicationContext, "The alarm has been set", Toast.LENGTH_SHORT).show()
            }
        }
    }
}