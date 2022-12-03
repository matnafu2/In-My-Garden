package com.example.inmygarden

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.inmygarden.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


// Binding to XML layout
private lateinit var binding: ActivityMainBinding
// View Model for keeping track of goals and their progress
 lateinit var goalsViewModel: GoalsViewModel
// View Model for keeping track of garden state
 lateinit var gardenviewModel: GardenViewModel

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

        /*
        // Either sets goals to defaults or retrieves goals set by user
        goalsViewModel.setDefaultGoals()

        */


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