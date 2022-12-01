package com.example.inmygarden

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView


class ManageGoalsActivity : AppCompatActivity() {


    private lateinit var goalsViewModel: GoalsViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_goals)

        goalsViewModel = ViewModelProvider(MainActivity())[GoalsViewModel::class.java]
        goalsViewModel.bindToActivityLifecycle(MainActivity())

        /*to help keep track of goals, they will all be displayed on a separate screen
        on this screen they can check off whether they completed the goal or want to remove it
        in progress
        */

        /*
        for ((key, value) in goalsViewModel.goals.value!!) {
            if (key == "Water") {
                val but = Button(this)
                but.setText(R.string.drink + value + R.string.ounces)
            } else if (key == "Steps") {
                val but = Button(this)
                but.setText(R.string.walk + value + R.string.steps)

            } else if (key == "Sleep") {
                val but = Button(this)
                but.setText(R.string.sleep + value + R.string.hours)
            } else {
                val but = Button(this)
                but.setText(value)
            }
        }

        */

        goalsViewModel.goals.observe(this) {

        }

    }

}