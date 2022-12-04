package com.example.inmygarden

import androidx.lifecycle.*

class GoalsViewModel : ViewModel(), DefaultLifecycleObserver {

    /*
     * goals live data is a map that maps a string describing the goals (e.g. steps)
     * to a pair of integers. The first integer is the current progress (likely
     * beginning at 0), and the second integer is the goal.
     */
    private val _goals: MutableLiveData<HashMap<String, Int>> =
        MutableLiveData<HashMap<String, Int>>()
    internal val goals: LiveData<HashMap<String, Int>>
        get() = _goals

    // Holds the current number of completed goals for the day
    var _dailyComplete: MutableLiveData<Int> =
        MutableLiveData<Int>()
    internal val dailyComplete: LiveData<Int>
        get() = _dailyComplete

    // Holds the current number of daily goals to accomplish
    private val _dailyTotal: MutableLiveData<Int> =
        MutableLiveData<Int>()
    internal val dailyTotal: LiveData<Int>
        get() = _dailyTotal

    init {
        _goals.value = HashMap<String, Int>()
        _dailyComplete.value = 0
        _dailyTotal.value = 0
    }

    /*
     * Either new data needs to be set, or data created from previous sessions needs to be loaded.
     */
    internal fun loadData() {
        if (true) {
            _goals.value = HashMap<String, Int>()
            _dailyComplete.value = 0
            _dailyTotal.value = 0
        } else {
            // load goals hashmap

            // load daily complete (Need to check if it's a new day)

            // load daily total
        }
    }

    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        mainActivity.lifecycle.addObserver(this)
    }

    internal fun dateUpdated() {
        _dailyComplete.value = 0
    }
}