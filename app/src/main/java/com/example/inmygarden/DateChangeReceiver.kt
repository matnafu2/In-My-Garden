package com.example.inmygarden

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DateChangeReceiver(private val goalsViewModel: GoalsViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action == Intent.ACTION_DATE_CHANGED) {
            goalsViewModel.dateUpdated()
        }
    }
}