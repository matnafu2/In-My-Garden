package com.example.inmygarden

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModelProvider
import com.example.inmygarden.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*


// Binding to XML layout
private lateinit var binding: ActivityMainBinding
// View Model for keeping track of goals and their progress
private lateinit var goalsViewModel: GoalsViewModel
// View Model for keeping track of garden state
<<<<<<< HEAD
private lateinit var gardenviewModel: GardenViewModel


=======
private lateinit var gardenViewModel: GardenViewModel
>>>>>>> a6322385ace69bcb54079ad5bd8beaaa3fe6f056

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use the provided ViewBinding class to inflate the layout.
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set content view to the binding root view.
        setContentView(binding.root)

        // Create goals view model
        goalsViewModel = ViewModelProvider(this)[GoalsViewModel::class.java]
        // Tie the GoalsViewModel to the MainActivity lifecycle
        goalsViewModel.bindToActivityLifecycle(this)

<<<<<<< HEAD
       // goalsViewModel.setDefaultGoals()

=======
>>>>>>> a6322385ace69bcb54079ad5bd8beaaa3fe6f056
        // Call function that watches for changes in the daily goal completion total
        beginObservingGoals()

        // Set button click listeners for navigation to other activities
        binding.goalsButton.setOnClickListener {
            val startGoalsIntent = Intent(this@MainActivity, GoalsActivity::class.java)
            startActivity(startGoalsIntent)
        }

        binding.gardenButton.setOnClickListener {
            val startGardenIntent = Intent(this, GardenActivity::class.java)
            startActivity(startGardenIntent)
        }

<<<<<<< HEAD
        /*
        // Either sets goals to defaults or retrieves goals set by user
        goalsViewModel.setDefaultGoals()

        */






=======
>>>>>>> a6322385ace69bcb54079ad5bd8beaaa3fe6f056
        // Create garden view model

        gardenViewModel = GardenViewModel()
        // Tie the GardenViewModel to the MainActivity lifecycle
        gardenViewModel.bindToActivityLifecycle(this)

        // Either sets goals to defaults or retrieves goals set by user
        gardenViewModel.setDefaultDays()
    }

    override fun onStart() {
        super.onStart()
        when (gardenViewModel.daysGrown.value) {
            1 -> binding.flower.setImageDrawable(R.drawable.flower1.toDrawable())
            2 -> binding.flower.setImageDrawable(R.drawable.flower2.toDrawable())
            3 -> binding.flower.setImageDrawable(R.drawable.flower3.toDrawable())
            4 -> binding.flower.setImageDrawable(R.drawable.flower4.toDrawable())
            5 -> binding.flower.setImageDrawable(R.drawable.flower5.toDrawable())
            6 -> binding.flower.setImageDrawable(R.drawable.flower6.toDrawable())
            7 -> binding.flower.setImageDrawable(R.drawable.flower7.toDrawable())
        }
    }

    private fun finishPlant() {
        gardenViewModel.growthComplete()
    }



    private fun beginObservingGoals() {
        // Set observer so that the text within the goals navigation button changes to reflect
        // the number of completed goals
        goalsViewModel.dailyComplete.observe(this) {
            with (binding.goalsButton) {
                text = getString(
                    R.string.goals_button,
                    goalsViewModel.dailyComplete.value.toString(),
                    goalsViewModel.dailyTotal.value.toString()
                )
            }
        }

        goalsViewModel.dailyTotal.observe(this) {
            with (binding.goalsButton) {
                text = getString(
                    R.string.goals_button,
                    goalsViewModel.dailyComplete.value.toString(),
                    goalsViewModel.dailyTotal.value.toString()
                )
            }
        }
    }

    // inflate the menu to the screen
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    // handles a click event from the menus
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // checks if the user clicks sign out
        if (item.itemId == R.id.signout) {
            // sign out the user
            val auth = FirebaseAuth.getInstance().signOut()

            // open the login activity after the user is signed out
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}