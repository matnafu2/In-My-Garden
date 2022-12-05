package com.example.inmygarden

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ManageGoalsActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_goals)
        sharedPrefs = this.getSharedPreferences("application", Context.MODE_PRIVATE)
        MainActivity.goalsViewModel.loadData(sharedPrefs)






        /*to help keep track of goals, they will all be displayed on a separate screen
        the goals can now probably be seen on this screen
        just need to add when they complete a goal or delete it, that data gets updated accordingly
        */

        val goals = sharedPrefs.getStringSet("Goals", mutableSetOf())
        val editor = sharedPrefs.edit()
        var newList : Set<String>

        goals!!.forEach { str ->
            val but = Button(this)
            but.text = str
            findViewById<LinearLayout>(R.id.goals_root).addView(but)
            but.setOnClickListener {
                val pop = PopupMenu(this, but)
                pop.menuInflater.inflate(R.menu.goals_menu, pop.menu)
                pop.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            MainActivity.goalsViewModel.subDailyTotal()
                            MainActivity.goalsViewModel.goals.value!!.remove(str, -1)

                            updateData()


                            Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
                            findViewById<LinearLayout>(R.id.goals_root).removeView(but)
                        }
                        R.id.complete -> {
                            MainActivity.goalsViewModel.addDailyComplete()
                            updateDailyComplete(MainActivity.goalsViewModel.dailyComplete.value!!)
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

        findViewById<Button>(R.id.goals_back).setOnClickListener {

            /*
            val intent = Intent(this@ManageGoalsActivity, GoalsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityIfNeeded(intent, 0)

             */

            // updateData()
            val intent = Intent(this@ManageGoalsActivity, GoalsActivity::class.java)
            startActivity(intent)
        }




    }

    private fun updateDailyComplete (int : Int) {
        val editor = sharedPrefs.edit()
        editor.putInt(R.string.daily_complete.toString(), int)
        editor.apply()
    }

    private fun updateData () {
        val editor = sharedPrefs.edit()

        MainActivity.goalsViewModel.goals.observe(this) {
            editor.remove("Goals")
            editor.commit()
            editor.putStringSet("Goals", MainActivity.goalsViewModel.goals.value!!.keys)
            editor.commit()
        }


    }


}