package com.example.inmygarden

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider


class ManageGoalsActivity : AppCompatActivity() {

    private lateinit var goalsViewModel: GoalsViewModel
    private lateinit var sharedPrefs: SharedPreferences


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_goals)
        goalsViewModel = ViewModelProvider(this)[GoalsViewModel::class.java]
        sharedPrefs = this.getSharedPreferences("application", Context.MODE_PRIVATE)
        goalsViewModel.loadData(sharedPrefs)




        /*to help keep track of goals, they will all be displayed on a separate screen
        the goals can now probably be seen on this screen
        just need to add when they complete a goal or delete it, that data gets updated accordingly
        */

        val goals = sharedPrefs.getStringSet("Goals", mutableSetOf())
        /*
        goalsViewModel.goals.value?.forEach { (key, value) ->
                    val but = Button(this)
                    but.text = key
                    findViewById<LinearLayout>(R.id.goals_root).addView(but)
                    but.setOnClickListener {
                        val pop = PopupMenu(this, but)
                        pop.menuInflater.inflate(R.menu.goals_menu, pop.menu)
                        pop.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.delete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                                R.id.complete -> {
                                    goalsViewModel.goals.value?.remove(key, value)
                                    goalsViewModel.addDailyComplete()
                                    updateDailyComplete(goalsViewModel.dailyComplete.value!!)
                                    Toast.makeText(this, "Goal Completed", Toast.LENGTH_SHORT)
                                        .show()
                                    findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                                }
                            }
                            true
                        }
                        pop.show()

                    }
        }
        */


        goals!!.forEach { it ->
            val but = Button(this)
            but.text = it
            findViewById<LinearLayout>(R.id.goals_root).addView(but)
            but.setOnClickListener {
                val pop = PopupMenu(this, but)
                pop.menuInflater.inflate(R.menu.goals_menu, pop.menu)
                pop.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            goalsViewModel.goals.value?.remove(it.toString(), -1)
                            Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
                            findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                        }
                        R.id.complete -> {
                            goalsViewModel.goals.value?.remove(it.toString(), -1)
                            goalsViewModel.addDailyComplete()
                            updateDailyComplete(goalsViewModel.dailyComplete.value!!)
                            Toast.makeText(this, "Goal Completed", Toast.LENGTH_SHORT)
                                .show()
                            findViewById<LinearLayout>(R.id.goals_root).removeView(but)

                        }
                    }
                    true
                }
                pop.show()

            }
        }

    }

    private fun updateDailyComplete (int : Int) {
        val editor = sharedPrefs.edit()
        editor.putInt(R.string.daily_complete.toString(), int)
        editor.apply()
    }

}