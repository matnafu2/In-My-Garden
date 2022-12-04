package com.example.inmygarden

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModelProvider
import com.example.inmygarden.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.util.*

// Binding to XML layout
private lateinit var binding: ActivityMainBinding
// View Model for keeping track of goals and their progress
private lateinit var goalsViewModel: GoalsViewModel
// View Model for keeping track of garden state
private lateinit var gardenviewModel: GardenViewModel


private lateinit var gardenViewModel: GardenViewModel
// Receiver for monitoring date changes
private lateinit var dateReceiver: DateChangeReceiver

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
        // Either sets goals to defaults or retrieves goals set by user
        goalsViewModel.loadData()

        // Create garden view model
        gardenViewModel = ViewModelProvider(this)[GardenViewModel::class.java]
        // Tie the GardenViewModel to the MainActivity lifecycle
        gardenViewModel.bindToActivityLifecycle(this)
        // Either sets goals to defaults or retrieves goals set by user
        gardenViewModel.loadData()

        // Initialize the broadcast receiver with the garden viewmodel
        dateReceiver = DateChangeReceiver(goalsViewModel)

       // goalsViewModel.setDefaultGoals()

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






        // Create garden view model
        gardenViewModel = ViewModelProvider(this)[GardenViewModel::class.java]
        // Tie the GardenViewModel to the MainActivity lifecycle
        gardenViewModel.bindToActivityLifecycle(this)

        // Either sets goals to defaults or retrieves goals set by user
        gardenViewModel.setDefaultDays()
=======
>>>>>>> d0f27168765f1f31cb24c63f23f7d9d39613ad76
    }

    override fun onStart() {
        super.onStart()

        registerReceiver(
            dateReceiver,
            IntentFilter(Intent.ACTION_DATE_CHANGED)
        )
    }

    override fun onResume() {
        super.onResume()
        /*
         * Goals could be completed for today, but we don't want the plant to show a new stage
         * of growth until tomorrow. This is to avoid it "ungrowing" if new goals are added.
         * The following if else block determines the value to be used for deciding which image
         * to display.
         */
        var stageNum: Int // Will determine image to display depending on stage of growth
        val currDate = LocalDate.now()

        // checks if goals were completed today
        if (gardenViewModel.lastDayGrown.value?.dayOfYear == currDate.dayOfYear &&
            gardenViewModel.lastDayGrown.value?.year == currDate.year) {
            stageNum = gardenViewModel.daysGrown.value?.minus(1)!!
        // Check if goals have been completed 7 days in a row
        // Days grown would have been incremented the day before, but now we grow/update
        } else if (gardenViewModel.daysGrown.value!! >= 8) {
            finishPlant()
            congratulationsDialog()
            stageNum = gardenViewModel.daysGrown.value!!
        } else {
            stageNum = gardenViewModel.daysGrown.value!!
        }

        when (stageNum) {
            1 -> binding.flower.setImageResource(R.drawable.flower1)
            2 -> binding.flower.setImageResource(R.drawable.flower2)
            3 -> binding.flower.setImageResource(R.drawable.flower3)
            4 -> binding.flower.setImageResource(R.drawable.flower4)
            5 -> binding.flower.setImageResource(R.drawable.flower5)
            6 -> binding.flower.setImageResource(R.drawable.flower6)
            7 -> binding.flower.setImageResource(R.drawable.flower7)
        }
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(dateReceiver)
    }

    private fun finishPlant() {
        gardenViewModel.growthComplete()
    }



    private fun beginObservingGoals() {
        goalsViewModel.dailyComplete.observe(this) {
            // Set observer so that the text within the goals navigation button changes to reflect
            // the number of completed goals
            with (binding.goalsButton) {
                text = getString(
                    R.string.goals_button,
                    goalsViewModel.dailyComplete.value.toString(),
                    goalsViewModel.dailyTotal.value.toString()
                )
            }

            val total = goalsViewModel.dailyTotal.value!! // Get total goals to complete in a day
            val completed = goalsViewModel.dailyComplete.value!! // Get goals currently completed
            /* If all goals are completed by the end of the day, the current day will be
             * considered a growth day. This means the view model should be updated to reflect
             * the addition of a successful day of growth (daysGrown++, update array, etc). Note
             * that the user could complete their goals then add more, changing today back from
             * a growth day, into an incomplete day.
             */
            var growthDay = false

            // Next, update the checkboxes as goals are completed
            if (total == 1) {
                if (completed > 0) {
                    // fill all checkboxes
                    fillCheckbox(arrayOf(1, 2, 3), true)
                    growthDay = true
                    goalsCompletedSnackbar()
                } else {
                    // empty all checkboxes
                    fillCheckbox(arrayOf(1, 2, 3), false)
                    growthDay = false
                }
            } else if (total == 2) {
                if (completed == 1) {
                    // fill first checkbox and empty others
                    fillCheckbox(arrayOf(1), true)
                    fillCheckbox(arrayOf(2, 3), false)
                    growthDay = false
                } else if (completed > 1){
                    // fill all checkboxes
                    fillCheckbox(arrayOf(1, 2, 3), true)
                    growthDay = true
                    goalsCompletedSnackbar()
                } else {
                    // empty all checkboxes
                    fillCheckbox(arrayOf(1, 2, 3), false)
                    growthDay = false
                }
            } else if (total >= 3) {
                // oneThird represents the number of goals that must be completed
                // to fill a checkbox
                val oneThird = total / 3

                if (completed == total) { // all goals completed for the day
                    // fill all three checkboxes
                    fillCheckbox(arrayOf(1, 2, 3), true)
                    growthDay = true
                    goalsCompletedSnackbar()
                } else if (completed % (oneThird * 2) == 0) { // two-thirds of goals completed
                    // fill first and second checkbox
                    fillCheckbox(arrayOf(1, 2), true)
                    // empty last checkbox
                    fillCheckbox(arrayOf(3), false)
                    growthDay = false
                } else if (completed % oneThird == 0) { // one third of goals completed
                    // fill first checkbox
                    fillCheckbox(arrayOf(1), true)
                    // empty other two checkboxes
                    fillCheckbox(arrayOf(2, 3), false)
                    growthDay = false
                } else { // no goals completed
                    // empty all checkboxes
                    fillCheckbox(arrayOf(1, 2, 3), false)
                    growthDay = false
                }
            } else {
                addGoalsSnackbar()
            }

            /* In the event that all daily goals are completed
             * or
             * Daily goals are no longer completed due to the addition of new daily goals
             * Update view model
             */
            gardenViewModel.updateGrowthDay(growthDay)
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

    /*
     * Used by the dailyComplete goals observer to update the number of checkboxes to fill.
     * Passed in is an array and a boolean.
     * The array contains which checkboxes to fill, order by top-down appearance on screen.
     * The boolean is true if the checkboxes are being filled, false if they are being emptied.
     */
    private fun fillCheckbox(pos: Array<Int>, fill: Boolean) {
        if (pos.contains(1)) {
            if (fill) {
                binding.waterCheckbox.setImageDrawable(R.drawable.checkbox_filled.toDrawable())
            } else {
                binding.waterCheckbox.setImageDrawable(R.drawable.checkbox_empty.toDrawable())
            }
        }
        if (pos.contains(2)) {
            if (fill) {
                binding.sunCheckbox.setImageDrawable(R.drawable.checkbox_filled.toDrawable())
            } else {
                binding.sunCheckbox.setImageDrawable(R.drawable.checkbox_empty.toDrawable())
            }
        }
        if (pos.contains(3)) {
            if (fill) {
                binding.foodCheckbox.setImageDrawable(R.drawable.checkbox_filled.toDrawable())
            } else {
                binding.foodCheckbox.setImageDrawable(R.drawable.checkbox_empty.toDrawable())
            }
        }
    }

    /*
     * The function is called within the dailyComplete observer if there are no goals to track.
     * It creates and displays a snackbar message telling the user to add goals.
     */
    private fun addGoalsSnackbar() {
        Snackbar.make(binding.flowerFrame,
            "Add some goals so your plant can grow!",
            Snackbar.LENGTH_SHORT).show()
    }

    /*
     * The function is called with the dailyComplete observer if all goals are completed for today.
     * It creates and displays a snackbar message telling the user to check back tomorrow to
     * see how that plant grows.
     */
    private fun goalsCompletedSnackbar() {
        Snackbar.make(binding.flowerFrame,
            "Goals Completed! Check your plant's growth tomorrow!",
            Snackbar.LENGTH_SHORT).show()
    }

    private fun congratulationsDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.congrats_title)
            .setMessage(R.string.congrats_text)
            .setPositiveButton("OKAY") { dialog, which ->

            }
            .show()
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