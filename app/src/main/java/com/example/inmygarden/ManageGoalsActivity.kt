package com.example.inmygarden

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity



class ManageGoalsActivity : AppCompatActivity() {



    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_goals)

        //goalsViewModel = GoalsViewModel()

        /*to help keep track of goals, they will all be displayed on a separate screen
        the goals can now probably be seen on this screen
        just need to add when they complete a goal or delete it, that data gets updated accordingly
        */



        goalsViewModel.goals.value?.forEach { (key, value) ->
            when (key) {
                "Water" -> {
                    val but = Button(this)
                    val str = "Drink " + value.toString() + " ounces"
                    but.text = str
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        findViewById<LinearLayout>(R.id.goals_root).removeView(but)
                        goalsViewModel.goals.value?.remove(key, value)
                    }
                }
                "Steps" -> {
                    val but = Button(this)
                    val str = "Walk " + value.toString() + " steps"
                    but.text = str
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        findViewById<LinearLayout>(R.id.goals_root).removeView(but)
                        goalsViewModel.goals.value?.remove(key, value)
                    }
                }
                "Sleep" -> {
                    val but = Button(this)
                    val str = "Sleep " + value.toString() + " hours"
                    but.text = str
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        findViewById<LinearLayout>(R.id.goals_root).removeView(but)
                        goalsViewModel.goals.value?.remove(key, value)
                    }
                }
                else -> {
                    val but = Button(this)
                    but.text = key
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        findViewById<LinearLayout>(R.id.goals_root).removeView(but)
                        goalsViewModel.goals.value?.remove(key, value)

                    }

                }
            }
        }

    }

}