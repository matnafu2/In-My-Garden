package com.example.inmygarden


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class GoalsActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences


    @SuppressLint("CutPasteId")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        sharedPrefs = this.getSharedPreferences("application", Context.MODE_PRIVATE)

        //here i am putting actions for each button pres
        //MainActivity.goalsViewModel.loadData(sharedPrefs)



        //when they add a custom goal
        findViewById<Button>(R.id.custom_button).setOnClickListener {
            val temp = findViewById<TextView>(R.id.text_custom).text.toString()

            if (temp == "") {
                Toast.makeText(this, "Not a valid goal!", Toast.LENGTH_SHORT).show()
            } else {
                MainActivity.goalsViewModel.addGoal(temp, false)
                MainActivity.goalsViewModel.addDailyTotal()

                MainActivity.goalsViewModel.updateData(sharedPrefs)

                findViewById<TextView>(R.id.text_custom).text = ""
                Toast.makeText(this, "Goal Added!", Toast.LENGTH_SHORT).show()
            }

        }


        findViewById<Button>(R.id.the_home_button).setOnClickListener {

            val intent = Intent(this@GoalsActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityIfNeeded(intent, 0)

        }

        findViewById<Button>(R.id.the_goals_button).setOnClickListener {
            val intent = Intent(this@GoalsActivity, ManageGoalsActivity::class.java)
            startActivity(intent)

        }
    }

}