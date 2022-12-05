package com.example.inmygarden

import android.content.SharedPreferences
import androidx.lifecycle.*
import java.time.LocalDate

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

    // Used to know if dailyComplete should be loaded or reset when loading in data
    private val _lastLogin: MutableLiveData<LocalDate> =
        MutableLiveData<LocalDate>()
    internal val lastLogin: LiveData<LocalDate>
        get() = _lastLogin

    init {
        _goals.value = HashMap<String, Int>()
        _dailyComplete.value = 0
        _dailyTotal.value = 3
    }


    internal fun addDailyComplete() {
        _dailyComplete.value = _dailyComplete.value!! + 1
    }

    internal fun addDailyTotal() {
        _dailyTotal.value = _dailyTotal.value!! + 1
    }

    internal fun addGoal(str : String, int : Int) {
        _goals.value!!.put(str, int)
    }


    /*
     * Either new data needs to be set, or data created from previous sessions needs to be loaded.
     */
    internal fun loadData(sharedPrefs: SharedPreferences) {
        var isPrevData = false
        if (sharedPrefs.contains(R.string.daily_total.toString())) {
            isPrevData = true
        }

        if (!isPrevData) {
            _goals.value = HashMap<String, Int>()
            _dailyComplete.value = 0
            _dailyTotal.value = 3
            _lastLogin.value = LocalDate.now()
        } else {
            // load goals hashmap

            // load daily complete (Need to check if it's a new day)
            val currDate = LocalDate.now()
            val lastDate = sharedPrefs.getString(R.string.last_day_grown.toString(), "01/01/1900")

            // if is same day as last login
            if (LocalDate.parse(lastDate).dayOfYear == currDate.dayOfYear &&
                LocalDate.parse(lastDate).year == currDate.year) {
                _dailyComplete.value = sharedPrefs.getInt(R.string.daily_complete.toString(), 0)
            } else { // BNew day, reset completed and last date of login
                _dailyComplete.value = 0
                _lastLogin.value = currDate
                sharedPrefs.edit().putString(
                    R.string.last_login_date.toString(),
                    _lastLogin.value?.toString()
                )
            }

            // load daily total
            _dailyTotal.value = sharedPrefs.getInt(R.string.daily_total.toString(), 0)
        }
    }

    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        mainActivity.lifecycle.addObserver(this)
    }

    internal fun dateUpdated() {
        _dailyComplete.value = 0
    }

    internal fun testGoalsComplete() {
        _dailyTotal.value = 4
        val temp = _dailyComplete.value!!
        _dailyComplete.value = (temp + 1)
    }

    internal fun testDayComplete() {
        dateUpdated()
    }
}