package com.example.inmygarden

import androidx.lifecycle.*

class GoalsViewModel : ViewModel(), DefaultLifecycleObserver {

    /*
     * goals live data is a map that maps a string describing the goals (e.g. steps)
     * to a pair of integers. The first integer is the current progress (likely
     * beginning at 0), and the second integer is the goal.
     */
    private val _goals: MutableLiveData<HashMap<String, Pair<Int, Int>>> =
        MutableLiveData<HashMap<String, Pair<Int, Int>>>()
    internal val goals: LiveData<HashMap<String, Pair<Int, Int>>>
        get() = _goals

    // Holds the current number of completed goals for the day
    private val _dailyComplete: MutableLiveData<Int> =
        MutableLiveData<Int>()
    internal val dailyComplete: LiveData<Int>
        get() = _dailyComplete

    // Holds the current number of daily goals to accomplish
    private val _dailyTotal: MutableLiveData<Int> =
        MutableLiveData<Int>()
    internal val dailyTotal: LiveData<Int>
        get() = _dailyTotal

    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        mainActivity.lifecycle.addObserver(this)
    }

    internal fun setDefaultGoals() {
        // if no goals set
        _goals.value = HashMap<String, Pair<Int, Int>>()
        _goals.value!!["Steps"] = Pair(0, 1000)
        _goals.value!!["Water"] = Pair(0, 6)
        _goals.value!!["Sleep"] = Pair(0, 8)

        _dailyComplete.value = 0
        _dailyTotal.value = 3
        // if goals set
        // get goals
    }
}