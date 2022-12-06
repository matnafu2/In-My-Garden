package com.example.inmygarden

import android.content.SharedPreferences
import android.util.Log
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
    private val _goals: MutableLiveData<HashMap<String, Boolean>> =
        MutableLiveData<HashMap<String, Boolean>>()
    internal val goals: LiveData<HashMap<String, Boolean>>
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
        _goals.value = HashMap<String, Boolean>()
        _dailyComplete.value = 0
        _dailyTotal.value = 0
    }
    //firebase database vars
    private var database: DatabaseReference =
        Firebase.database.reference
    private var userId: String =
        FirebaseAuth.getInstance().uid.toString()


    internal fun addDailyComplete(key: String) {
        if (!goals.value!![key]!!) {
            _dailyComplete.value = _dailyComplete.value!! + 1
            goals.value!![key] = true
        }
    }

    internal fun addDailyTotal() {
        _dailyTotal.value = _dailyTotal.value!! + 1
    }

    internal  fun subDailyTotal() {
        _dailyTotal.value = _dailyTotal.value!! - 1
    }

    internal fun addGoal(str : String, completed : Boolean) {
        _goals.value!![str] = completed
    }

    internal  fun removeGoal(str : String) {
        _goals.value!!.remove(str)
    }


    /*test
     * Either new data needs to be set, or data created from previous sessions needs to be loaded.
     */
    internal fun loadMapFromFirebase() {
        val userData = database.child("goals").child(userId)
        userData.child("goalsData").get().addOnSuccessListener {
            if (it.value != null) {
                _goals.value = it.value as HashMap<String, Boolean>?
            } else {
                _goals.value = HashMap<String, Boolean>()
            }
        }.addOnFailureListener {
            _goals.value = HashMap<String, Boolean>()
        }
    }
    internal fun loadData(sharedPrefs: SharedPreferences) {
        var isPrevData = false
        if (sharedPrefs.contains(R.string.daily_total.toString())) {
            isPrevData = true
        }

        if (!isPrevData) {
            _goals.value = HashMap<String, Boolean>()
            _dailyComplete.value = 0
            _dailyTotal.value = 0
            _lastLogin.value = LocalDate.now()
        } else {
            // load goals hashmap
            loadMapFromFirebase()
            // load daily complete (Need to check if it's a new day)
            val currDate = LocalDate.now()
            Log.i("currdate", "$currDate")
            val lastDate = sharedPrefs.getString(R.string.last_login_date.toString(), "1900-01-01")

            // if is same day as last login
            if (LocalDate.parse(lastDate).dayOfYear == currDate.dayOfYear &&
                LocalDate.parse(lastDate).year == currDate.year) {
                _dailyComplete.value = sharedPrefs.getInt(R.string.daily_complete.toString(), 0)
            } else { // BNew day, reset completed and last date of login
                _dailyComplete.value = 0
                _lastLogin.value = currDate
                var editor = sharedPrefs.edit()
                editor.putString(
                    R.string.last_login_date.toString(),
                    _lastLogin.value?.toString()
                )
                editor.commit()
                editor.apply()
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
        for (key in _goals.value?.keys!!) {
            _goals.value!![key] = false
        }
    }

    internal fun testGoalsComplete() {
        _dailyTotal.value = 4
        val temp = _dailyComplete.value!!
        if (_dailyComplete.value?.compareTo(_dailyTotal.value!!)!! < 0) {
            _dailyComplete.value = (temp + 1)
        }
    }

    internal fun updateData (pref : SharedPreferences) {
        val editor = pref.edit()

        //editor.putStringSet("Goals", _goals.value!!.keys)
        //editor.commit()

        editor.putInt(R.string.daily_total.toString(), _dailyTotal.value!!)
        editor.commit()

        editor.putInt(R.string.daily_complete.toString(), _dailyComplete.value!!)
        editor.commit()

        editor.putString(
            R.string.last_login_date.toString(),
            _lastLogin.value?.toString()
        )
        editor.commit()
        postMapToFirebase()
        editor.apply()

    }
    internal fun postMapToFirebase () {
        val userData = database.child("goals").child(userId)
        userData.child("goalsData").setValue(_goals.value)
    }

    internal fun resetGoals() {
        _dailyComplete.value = 0
        _dailyTotal.value = 0
        _goals.value!!.clear()
        postMapToFirebase()
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