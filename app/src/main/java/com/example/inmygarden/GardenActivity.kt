package com.example.inmygarden

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inmygarden.databinding.ActivityGardenBinding

class GardenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGardenBinding
    private lateinit var flowerArray: Array<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGardenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val returnToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(returnToMainActivity)
        }
        flowerArray = arrayOf(binding.flower1, binding.flower2, binding.flower3, binding.flower4,
            binding.flower5, binding.flower6, binding.flower7, binding.flower8, binding.flower9)
        observePlant()
    }
    private fun observePlant() {
        gardenViewModel.plantFinished.observe(this) {
            if (it) {
                for (flower in flowerArray) {
                    if (flower.visibility == View.INVISIBLE) {
                        flower.visibility = View.VISIBLE
                        makeToast()
                        Log.i("IMG", "flower set to visible")
                        break
                    }
                }
            }
        }
    }
    private fun makeToast() {
        Toast.makeText(applicationContext, "Your plant has been added to the garden!",
            Toast.LENGTH_SHORT).show()
    }
    companion object {
        val gardenViewModel = GardenViewModel()
    }
}