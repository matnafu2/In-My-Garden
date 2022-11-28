package com.example.inmygarden

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class GoalsActivity : AppCompatActivity() {

    //buttons and text boxes for the goals page

    private lateinit var btnWater: Button
    private lateinit var btnSleep: Button
    private lateinit var btnSteps: Button
    private lateinit var btnCustom: Button
    private lateinit var btnHome: Button

    private lateinit var editWater: EditText
    private lateinit var editSleep: EditText
    private lateinit var editSteps: EditText
    private lateinit var editCustom: EditText

    private lateinit var goalsViewModel: GoalsViewModel



    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)


        goalsViewModel = ViewModelProvider(MainActivity())[GoalsViewModel::class.java]
        goalsViewModel.bindToActivityLifecycle(MainActivity())

        btnWater = findViewById(R.id.water_button)
        btnSleep = findViewById(R.id.sleep_button)
        btnSteps = findViewById(R.id.steps_button)
        btnCustom = findViewById(R.id.custom_button)
        btnHome = findViewById(R.id.home_button)

        editWater = findViewById(R.id.text_water)
        editSleep = findViewById(R.id.text_sleep)
        editSteps = findViewById(R.id.text_steps)
        editCustom = findViewById(R.id.text_custom)

        //here i am putting actions for each button press .. in progress

        btnWater.setOnClickListener {
            val temp = editWater.text
            goalsViewModel.goals.value!!["Water"] = temp.toString().toInt()
            goalsViewModel.dailyTotal.value!!.plus(1)
            editWater.setText("")
        }

        btnSleep.setOnClickListener {
            val temp = editSleep.text
            goalsViewModel.goals.value!!["Sleep"] = temp.toString().toInt()
            goalsViewModel.dailyTotal.value!!.plus(1)
            editSleep.setText("")
        }

        btnSteps.setOnClickListener {
            val temp = editSteps.text
            goalsViewModel.goals.value!!["Steps"] = temp.toString().toInt()
            goalsViewModel.dailyTotal.value!!.plus(1)
            editSteps.setText("")
        }

        btnCustom.setOnClickListener {
            val temp = editCustom.text

            editSteps.setText("")
        }

        btnHome.setOnClickListener {

        }


    }

}