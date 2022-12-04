package com.example.inmygarden

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.inmygarden.databinding.ActivityGardenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GardenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGardenBinding
    private lateinit var flowerArray: Array<ImageView>
    private lateinit var potArray: Array<ImageView>
    //private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //database = Firebase.database.reference
        //userId = FirebaseAuth.getInstance().uid.toString()
        sharedPreferences = this.getSharedPreferences("application", Context.MODE_PRIVATE)
        binding = ActivityGardenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val returnToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(returnToMainActivity)
        }
        binding.changeColor.setOnClickListener {
            changePotColors()
        }
        binding.clearGarden.setOnClickListener{
            clearGarden()
        }
        flowerArray = arrayOf(binding.flower1, binding.flower2, binding.flower3, binding.flower4,
            binding.flower5, binding.flower6, binding.flower7, binding.flower8, binding.flower9)

        potArray = arrayOf(binding.pot1, binding.pot2, binding.pot3, binding.pot4,
            binding.pot5, binding.pot6, binding.pot7, binding.pot8, binding.pot9)

        pullFromSharedPreferences()
        observePlant()
    }
    override fun onResume() {
        super.onResume()
        pullFromSharedPreferences()
    }
    // when the "change pot colors" is clicked, user then has to click on the pot
    // they want to change, then an overlay view is made visible in which they
    // can select the color they want
    private fun changePotColors() {
        Toast.makeText(applicationContext, "Click on a pot to change its color!",
            Toast.LENGTH_SHORT).show()
        for (pot in potArray) {
            pot.setOnClickListener {
                binding.overlay.visibility = View.VISIBLE
                binding.overlay.bringToFront()
                binding.clearGarden.visibility = View.INVISIBLE
                binding.changeColor.visibility = View.INVISIBLE
                binding.blue.setOnClickListener {
                    binding.potSample.setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.MULTIPLY)
                    pot.setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.MULTIPLY)
                }
                binding.red.setOnClickListener {
                    binding.potSample.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY)
                    pot.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY)
                }
                binding.yellow.setOnClickListener {
                    binding.potSample.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
                    pot.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
                }
                binding.brown.setOnClickListener {
                    pot.clearColorFilter()
                    binding.potSample.clearColorFilter()
                }
            }
        }
        binding.applyChanges.setOnClickListener {
            binding.potSample.clearColorFilter()
            binding.overlay.visibility = View.INVISIBLE
            binding.clearGarden.visibility = View.VISIBLE
            binding.changeColor.visibility = View.VISIBLE

        }

    }
    private fun observePlant() {
        gardenViewModel.plantFinished.observe(this) {
            if (it) {
                var check = false
                for (flower in flowerArray) {
                    if (flower.visibility == View.INVISIBLE) {
                        flower.visibility = View.VISIBLE
                        makeToast()
                        updateSharedPreferences(flower.id.toString(), true)
                        //Log.i("IMG", "flower set to visible")
                        check = true
                        break
                    }
                }
                if (check == false) makeErrorToast()
            }
        }
        // Adding a call to set platFinished to false after it has been handled
        gardenViewModel.resetPlantFinished()
    }
    private fun makeToast() {
        Toast.makeText(applicationContext, "Your plant has been added to the garden!",
            Toast.LENGTH_SHORT).show()
    }
    private fun makeErrorToast() {
        Toast.makeText(applicationContext, "Your garden is full! Clear garden to make space",
            Toast.LENGTH_SHORT).show()
    }
    private fun makeClearToast() {
        Toast.makeText(applicationContext, "Cleared Garden",
            Toast.LENGTH_SHORT).show()
    }
    private fun updateSharedPreferences(id: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(id, value)
        editor.apply()
        //Log.i("IMG", "$id is stored as visible")
    }
    private fun pullFromSharedPreferences() {
        for (flower in flowerArray) {
            if (sharedPreferences.getBoolean(flower.id.toString(), false)) {
                Log.i("IMG", "${(flower.id.toString())} is visible ")
                flower.visibility = View.VISIBLE
            }
            else {
                //Log.i("IMG", "${(flower.id.toString())} is invisible ")
            }
        }
    }

    private fun clearGarden() {
        val editor = sharedPreferences.edit()
        for (flower in flowerArray) {
            flower.visibility = View.INVISIBLE
            editor.putBoolean(flower.id.toString(), false)
        }
        makeClearToast()
        editor.apply()
    }
    companion object {
        val gardenViewModel = GardenViewModel()
    }
}