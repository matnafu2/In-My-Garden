package com.example.inmygarden

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DateChangeReceiver(private val gardenViewModel: GardenViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            gardenViewModel.updateDate()
        }
    }

}