package com.example.inmygarden

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inmygarden.databinding.ActivityGardenBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GardenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGardenBinding
    private lateinit var flowerArray: Array<ImageView>
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
        sharedPreferences = this.getSharedPreferences("application", Context.MODE_PRIVATE)
        binding = ActivityGardenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val returnToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(returnToMainActivity)
        }
        binding.clearGarden.setOnClickListener{
            clearGarden()
        }
        flowerArray = arrayOf(binding.flower1, binding.flower2, binding.flower3, binding.flower4,
            binding.flower5, binding.flower6, binding.flower7, binding.flower8, binding.flower9)
        pullFromSharedPreferences()
        observePlant()
    }
    override fun onDataChange(){

    }

    override fun onResume() {
        super.onResume()
        pullFromSharedPreferences()
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
        Log.i("IMG", "$id is stored as visible")
    }
    private fun pullFromSharedPreferences() {
        for (flower in flowerArray) {
            if (sharedPreferences.getBoolean(flower.id.toString(), false)) {
                Log.i("IMG", "${(flower.id.toString())} is visible ")
                flower.visibility = View.VISIBLE
            }
            else {
                Log.i("IMG", "${(flower.id.toString())} is invisible ")
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