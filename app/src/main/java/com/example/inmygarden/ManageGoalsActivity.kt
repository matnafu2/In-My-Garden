package com.example.inmygarden

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class ManageGoalsActivity : AppCompatActivity() {

//private lateinit var goalsViewModel : GoalsViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_goals)
        ///goalsViewModel = ViewModelProvider(mainActivity)[GoalsViewModel::class.java]


        /*to help keep track of goals, they will all be displayed on a separate screen
        the goals can now probably be seen on this screen
        just need to add when they complete a goal or delete it, that data gets updated accordingly
        */



        goalsViewModel.goals.value?.forEach { (key, value) ->
            when (key) {

                //when goal is water
                "Water" -> {

                    //this will display the goal
                    val but = Button(this)
                    val str = "Drink " + value.toString() + " ounces"
                    but.text = str
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)

                    //i use this to handle how the user will interact with goal
                    but.setOnClickListener {
                        val pop = PopupMenu(this, but)

                        //creating menu with option to complete or delete goal
                        pop.menuInflater.inflate(R.menu.goals_menu, pop.menu)
                        pop.setOnMenuItemClickListener { item ->
                            when(item.itemId){

                                //when they delete goal
                                R.id.delete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }

                                //when they complete goal
                                R.id.complete -> {
                                    goalsViewModel.goals.value?.remove(key, value)

                                    //increment the amount of goals they completed
                                    goalsViewModel.addDailyComplete()
                                    Toast.makeText(this, "Goal Completed", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                            }
                            true
                        }
                        pop.show()


                        /*
                        findViewById<LinearLayout>(R.id.goals_root).removeView(but)
                        goalsViewModel.goals.value?.remove(key, value)

                         */
                    }
                }
                "Steps" -> {
                    val but = Button(this)
                    val str = "Walk " + value.toString() + " steps"
                    but.text = str
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        val pop = PopupMenu(this, but)
                        pop.menuInflater.inflate(R.menu.goals_menu, pop.menu)
                        pop.setOnMenuItemClickListener { item ->
                            when(item.itemId){
                                R.id.delete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                                R.id.complete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    goalsViewModel.addDailyComplete()
                                    Toast.makeText(this, "Goal Completed", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                            }
                            true
                        }
                        pop.show()
                    }
                }
                "Sleep" -> {
                    val but = Button(this)
                    val str = "Sleep " + value.toString() + " hours"
                    but.text = str
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        val pop = PopupMenu(this, but)
                        pop.menuInflater.inflate(R.menu.goals_menu, pop.menu)
                        pop.setOnMenuItemClickListener { item ->
                            when(item.itemId){
                                R.id.delete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                                R.id.complete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    goalsViewModel.addDailyComplete()
                                    Toast.makeText(this, "Goal Completed", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                            }
                            true
                        }
                        pop.show()
                    }
                }
                else -> {
                    val but = Button(this)
                    but.text = key
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        val pop = PopupMenu(this, but)
                        pop.menuInflater.inflate(R.menu.goals_menu, pop.menu)
                        pop.setOnMenuItemClickListener { item ->
                            when(item.itemId){
                                R.id.delete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                                R.id.complete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    goalsViewModel.addDailyComplete()
                                    Toast.makeText(this, "Goal Completed", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                            }
                            true
                        }
                        pop.show()

                    }

                }
            }
        }

    }

}