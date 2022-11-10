package com.example.inmygarden

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.inmygarden.databinding.ActivityMainBinding


// Binding to XML layout
private lateinit var binding: ActivityMainBinding
// View Model for keeping track of goals and their progress
private lateinit var goalsViewModel: GoalsViewModel
// View Model for keeping track of garden state
private lateinit var gardenviewModel: GardenViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//<<<<<<< HEAD
        setContentView(R.layout.activity_main)

        //this is a test
//=======

        // Use the provided ViewBinding class to inflate the layout.
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set content view to the binding root view.
        setContentView(binding.root)

        // Set button click listeners for navigation to other activities
        binding.goalsButton.setOnClickListener {
//            val startGoalsIntent = Intent(this@MainActivity, GoalsActivity::class.java)
//            startActivity(startGoalsIntent)
        }

        binding.gardenButton.setOnClickListener {
//            val startGardenIntent = Intent(this@MainActivity, GardenActivity::class.java)
//            startActivity(startGardenIntent)
        }

        // Create goals view model
        goalsViewModel = GoalsViewModel()
        // Tie the GoalsViewModel to the MainActivity lifecycle
        goalsViewModel.bindToActivityLifecycle(this)

        // Call function that watches for changes in the daily goal completion total
        beginObservingGoals()

        // Either sets goals to defaults or retrieves goals set by user
        goalsViewModel.setDefaultGoals()

        // Create garden view model
        gardenviewModel = GardenViewModel()
        // Tie the GardenViewModel to the MainActivity lifecycle
        gardenviewModel.bindToActivityLifecycle(this)
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
//>>>>>>> 6bcee9ab7022f21162e6db7a978045159d6a238e
    }
}