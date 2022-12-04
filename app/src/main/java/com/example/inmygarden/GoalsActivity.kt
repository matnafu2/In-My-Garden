package com.example.inmygarden


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class GoalsActivity : AppCompatActivity() {


    //private lateinit var goalsViewModel: GoalsViewModel


    class GoalsActivity : AppCompatActivity() {

        private lateinit var goalsViewModel: GoalsViewModel


        @SuppressLint("CutPasteId")
        public override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_goals)

            //i did this for some reason
            val i = 1

            // here i am putting actions for each button pres
            goalsViewModel = ViewModelProvider(this)[GoalsViewModel::class.java]


            //when they add water goal
            findViewById<Button>(R.id.water_button).setOnClickListener {


                val temp = findViewById<TextView>(R.id.text_water).text.toString()



                if (temp.toIntOrNull() == null || temp == "") {
                    Toast.makeText(this, "Not a valid number!", Toast.LENGTH_SHORT).show()
                } else {


                    if (goalsViewModel.goals.value?.containsKey("Water") == false) {
                        goalsViewModel.addDailyTotal()
                    }

<<<<<<< HEAD
                    goalsViewModel.goals.value?.put("Water", temp.toInt())
                    findViewById<TextView>(R.id.text_water).text = ""
                    Toast.makeText(
                        this,
                        "Total Goals: " + goalsViewModel.dailyTotal.value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
=======
        //i did this for some reason
        val i = 1

        //here i am putting actions for each button pres
//        goalsViewModel = ViewModelProvider(mainActivity)[GoalsViewModel::class.java]
        goalsViewModel = ViewModelProvider(this)[GoalsViewModel::class.java]


        //when they add water goal
        findViewById<Button>(R.id.water_button).setOnClickListener {


            val temp = findViewById<TextView>(R.id.text_water).text.toString()

            if (temp.toIntOrNull() == null || temp == "") {
                Toast.makeText(this,"Not a valid number!", Toast.LENGTH_SHORT).show()
            } else {
                goalsViewModel.goals.value?.put("Water", temp.toInt())
                goalsViewModel.dailyTotal.value?.plus(i)
                findViewById<TextView>(R.id.text_water).text = ""
                Toast.makeText(this, "Goal added!", Toast.LENGTH_SHORT).show()
            }

        }


        //when they add steps goal
        findViewById<Button>(R.id.steps_button).setOnClickListener {
            val temp = findViewById<TextView>(R.id.text_steps).text.toString()

            if (temp.toIntOrNull() == null || temp == "") {
                Toast.makeText(this, "Not a valid number!", Toast.LENGTH_SHORT).show()
            } else {
                goalsViewModel.goals.value?.put("Steps", temp.toInt())
                goalsViewModel.dailyTotal.value?.plus(i)
                findViewById<TextView>(R.id.text_steps).text = ""
                Toast.makeText(this, "Goal added!", Toast.LENGTH_SHORT).show()
            }
        }


        //when they add sleep goal
        findViewById<Button>(R.id.sleep_button).setOnClickListener {
            val temp = findViewById<TextView>(R.id.text_sleep).text.toString()

            if (temp.toIntOrNull() == null || temp == "") {
                Toast.makeText(this, "Not a valid number!", Toast.LENGTH_SHORT).show()
            } else {
                goalsViewModel.goals.value?.put("Sleep", temp.toInt())
                goalsViewModel.dailyTotal.value?.plus(i)
                findViewById<TextView>(R.id.text_sleep).text = ""
                Toast.makeText(this, "Goal added!", Toast.LENGTH_SHORT).show()
>>>>>>> ae4d92e4a77c2893ceca413f7c0711c8dd7e0dfa

            }


            //when they add steps goal
            findViewById<Button>(R.id.steps_button).setOnClickListener {
                val temp = findViewById<TextView>(R.id.text_steps).text.toString()

                if (temp.toIntOrNull() == null || temp == "") {
                    Toast.makeText(this, "Not a valid number!", Toast.LENGTH_SHORT).show()
                } else {

                    if (goalsViewModel.goals.value?.containsKey("Steps") == false) {
                        goalsViewModel.addDailyTotal()
                    }

                    goalsViewModel.goals.value?.put("Steps", temp.toInt())
                    findViewById<TextView>(R.id.text_steps).text = ""
                    Toast.makeText(
                        this,
                        "Total Goals: " + goalsViewModel.dailyTotal.value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            //when they add sleep goal
            findViewById<Button>(R.id.sleep_button).setOnClickListener {
                val temp = findViewById<TextView>(R.id.text_sleep).text.toString()

                if (temp.toIntOrNull() == null || temp == "") {
                    Toast.makeText(this, "Not a valid number!", Toast.LENGTH_SHORT).show()
                } else {

                    if (goalsViewModel.goals.value?.containsKey("Sleep") == false) {
                        goalsViewModel.addDailyTotal()
                    }

                    goalsViewModel.goals.value?.put("Sleep", temp.toInt())
                    findViewById<TextView>(R.id.text_sleep).text = ""
                    Toast.makeText(
                        this,
                        "Total Goals: " + goalsViewModel.dailyTotal.value,
                        Toast.LENGTH_SHORT
                    ).show()

                }


            }


            //when they add a custom goal
            findViewById<Button>(R.id.custom_button).setOnClickListener {
                val temp = findViewById<TextView>(R.id.text_custom).text.toString()

                if (temp == "") {
                    Toast.makeText(this, "Not a valid goal!", Toast.LENGTH_SHORT).show()
                } else {
                    goalsViewModel.goals.value?.put(temp, -1)
                    goalsViewModel.addDailyTotal()
                    findViewById<TextView>(R.id.text_custom).text = ""
                    Toast.makeText(
                        this,
                        "Total Goals: " + goalsViewModel.dailyTotal.value,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            findViewById<Button>(R.id.the_home_button).setOnClickListener {
                /*
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

             */
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivityIfNeeded(intent, 0)
                50
            }

            findViewById<Button>(R.id.the_goals_button).setOnClickListener {
                val intent = Intent(this@GoalsActivity, ManageGoalsActivity::class.java)
                startActivity(intent)
            }

        }

    }
}