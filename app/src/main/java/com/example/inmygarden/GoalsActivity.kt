package com.example.inmygarden


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity






class GoalsActivity : AppCompatActivity() {

    //private lateinit var goalsViewModel: GoalsViewModel





    @SuppressLint("CutPasteId")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        val i = 1

        //here i am putting actions for each button press


        findViewById<Button>(R.id.water_button).setOnClickListener {


            val temp = findViewById<TextView>(R.id.text_water).text.toString().toInt()
            goalsViewModel.goals.value?.put("Water", temp)
            goalsViewModel.dailyTotal.value?.plus(i)
            findViewById<TextView>(R.id.text_water).text = ""
        }


        findViewById<Button>(R.id.steps_button).setOnClickListener {
            val temp = findViewById<TextView>(R.id.text_steps).text.toString().toInt()
            goalsViewModel.goals.value?.put("Steps", temp)
            goalsViewModel.dailyTotal.value?.plus(i)
            findViewById<TextView>(R.id.text_steps).text = ""
        }


        findViewById<Button>(R.id.sleep_button).setOnClickListener {
            val temp = findViewById<TextView>(R.id.text_sleep).text.toString().toInt()
            goalsViewModel.goals.value?.put("Sleep", temp)
            goalsViewModel.dailyTotal.value?.plus(i)
            findViewById<TextView>(R.id.text_sleep).text = ""
        }

        findViewById<Button>(R.id.custom_button).setOnClickListener {
            val temp = findViewById<TextView>(R.id.text_custom).text.toString()
            goalsViewModel.goals.value?.put(temp, -1)
            goalsViewModel.dailyTotal.value?.plus(i)
            findViewById<TextView>(R.id.text_custom).text = ""
        }

        findViewById<Button>(R.id.the_home_button).setOnClickListener {
            val intent = Intent(this@GoalsActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.the_goals_button).setOnClickListener {
            val intent = Intent(this@GoalsActivity, ManageGoalsActivity::class.java)
            startActivity(intent)
        }

    }

}