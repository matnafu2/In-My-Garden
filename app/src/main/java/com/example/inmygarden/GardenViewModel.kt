package com.example.inmygarden

import androidx.lifecycle.*

class GardenViewModel : ViewModel(), DefaultLifecycleObserver {
    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        mainActivity.lifecycle.addObserver(this)
    }
}