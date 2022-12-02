package com.example.inmygarden

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider



class ManageGoalsActivity : AppCompatActivity() {


    private lateinit var goalsViewModel: GoalsViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_goals)

        goalsViewModel = ViewModelProvider(MainActivity())[GoalsViewModel::class.java]
        goalsViewModel.bindToActivityLifecycle(MainActivity())

        /*to help keep track of goals, they will all be displayed on a separate screen
        the goals can now probably be seen on this screen
        just need to add when they complete a goal or delete it, that data gets updated accordingly
        */

        for ((key, value) in goalsViewModel.goals.value!!) {
            when (key) {
                "Water" -> {
                    val but = Button(this)
                    but.setText(R.string.drink + value + R.string.ounces)
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                }
                "Steps" -> {
                    val but = Button(this)
                    but.setText(R.string.walk + value + R.string.steps)
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                }
                "Sleep" -> {
                    val but = Button(this)
                    but.setText(R.string.sleep + value + R.string.hours)
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                }
                else -> {
                    val but = Button(this)
                    but.setText(value)
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                }
            }
        }

    }

}