package com.example.inmygarden


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.inmygarden.databinding.ActivityGoalsBinding



class GoalsActivity : AppCompatActivity() {

    private lateinit var goalsViewModel: GoalsViewModel
    private lateinit var binding: ActivityGoalsBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goalsViewModel = ViewModelProvider(MainActivity())[GoalsViewModel::class.java]
        goalsViewModel.bindToActivityLifecycle(MainActivity())


        //here i am putting actions for each button press

        binding.waterButton.setOnClickListener {
            val temp = binding.textWater.text
            goalsViewModel.goals.value!!["Water"] = temp.toString().toInt()
            goalsViewModel.dailyTotal.value!!.plus(1)
            binding.textWater.setText("")
        }

        binding.sleepButton.setOnClickListener {
            val temp = binding.textSleep.text
            goalsViewModel.goals.value!!["Sleep"] = temp.toString().toInt()
            goalsViewModel.dailyTotal.value!!.plus(1)
            binding.textSleep.setText("")
        }

        binding.stepsButton.setOnClickListener {
            val temp = binding.textSteps.text
            goalsViewModel.goals.value!!["Steps"] = temp.toString().toInt()
            goalsViewModel.dailyTotal.value!!.plus(1)
            binding.textSteps.setText("")
        }

        binding.customButton.setOnClickListener {
            val temp = binding.textCustom.text
            goalsViewModel.goals.value?.put(temp.toString(), -1)
            goalsViewModel.dailyTotal.value!!.plus(1)
            binding.textCustom.setText("")
        }

        binding.homeButton.setOnClickListener {
            val intent = Intent(this@GoalsActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.goalsButton.setOnClickListener {
            val intent = Intent(this@GoalsActivity, ManageGoalsActivity::class.java)
            startActivity(intent)
        }

    }

}