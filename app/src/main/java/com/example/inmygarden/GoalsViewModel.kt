package com.example.inmygarden

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
    //firebase database vars
    private var database: DatabaseReference =
        Firebase.database.reference
    private var userId: String =
        FirebaseAuth.getInstance().uid.toString()


    internal fun addDailyComplete() {
        _dailyComplete.value = _dailyComplete.value!! + 1
    }

    internal fun addDailyTotal() {
        _dailyTotal.value = _dailyTotal.value!! + 1
    }

    internal  fun subDailyTotal() {
        _dailyTotal.value = _dailyTotal.value!! - 1
    }

    internal fun addGoal(str : String, int : Int) {
        _goals.value!![str] = int
    }

    internal  fun removeGoal(str : String) {
        _goals.value!!.remove(str)
    }


    /*test
     * Either new data needs to be set, or data created from previous sessions needs to be loaded.
     */
    /*
    internal fun loadData() {
        val userData = database.child("goals").child(userId)
        //get the goals data from firebase, if available
        userData.child("goalsData").get().addOnSuccessListener {
            if (it.value != null) {
                _goals.value = it.value as HashMap<String, Int>?
            } else {
                _goals.value = HashMap<String, Int>()
            }
        }.addOnFailureListener {
            _goals.value = HashMap<String, Int>()
        }
        val currDate = LocalDate.now()
        //get last date from firebase if available
        userData.child("lastDate").get().addOnSuccessListener { storedDate ->
            if (storedDate.value != null && storedDate.value != "null") {
                val lastDate = storedDate.value as String
                // if is same day as last login, then get the saved daily complete if avail
                if (LocalDate.parse(lastDate).dayOfYear == currDate.dayOfYear &&
                    LocalDate.parse(lastDate).year == currDate.year
                ) {
                    userData.child("dailyComplete").get().addOnSuccessListener {
                        if (it.value != null) {
                            _dailyComplete.value = it.value as Int
                        } else {
                            _dailyComplete.value = 0
                            _lastLogin.value = currDate
                        }
                    }.addOnFailureListener {
                        _dailyComplete.value = 0
                        _lastLogin.value = currDate
                    }
                }
            } else {
                _dailyComplete.value = 0
                _lastLogin.value = currDate
            }
        }.addOnFailureListener() {
            _dailyComplete.value = 0
            _lastLogin.value = currDate
        }
        userData.child("dailyTotal").get().addOnSuccessListener {
            if (it.value != null && it.value != "null") {
                _dailyTotal.value = it.value as Int
            }
            //_dailyTotal.value = sharedPrefs.getInt(R.string.daily_total.toString(), 0)

        }
    }*/
    internal fun loadData(sharedPrefs: SharedPreferences) {
        var isPrevData = false
        if (sharedPrefs.contains(R.string.daily_total.toString())) {
            isPrevData = true
        }

        if (!isPrevData) {
            _goals.value = HashMap<String, Int>()
            _dailyComplete.value = 0
            _dailyTotal.value = 0
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
        if (_dailyComplete.value?.compareTo(_dailyTotal.value!!)!! < 0) {
            _dailyComplete.value = (temp + 1)
        }
    }

    internal fun testDayComplete() {
        dateUpdated()
    } /*
    val goalsMapListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }
    val dailyCompleteListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }*/
}