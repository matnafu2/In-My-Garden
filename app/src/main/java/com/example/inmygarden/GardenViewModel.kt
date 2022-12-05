package com.example.inmygarden

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import java.time.LocalDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GardenViewModel : ViewModel(), DefaultLifecycleObserver {

    /*
     * daysGrown live data keeps track of the stage of growth that the flower is in out of 7 stages
     * (one week). This will allow for the proper stage of growth to be displayed, and for
     * knowing when to start a new plant.
     */
    private val _plantsFinished: MutableLiveData<Int> =
        MutableLiveData<Int>(0)
    internal val plantsFinished: LiveData<Int>
        get() = _plantsFinished

    private val _daysGrown: MutableLiveData<Int> =
        MutableLiveData<Int>(1)
    internal val daysGrown: LiveData<Int>
        get() = _daysGrown

    private var database: DatabaseReference =
        Firebase.database.reference
    private var userId: String =
        FirebaseAuth.getInstance().uid.toString()

    private val _plantFinished: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>(false)
    internal val plantFinished: LiveData<Boolean>
        get() = _plantFinished

    /*
     * lastDayGrown will keep track of the most recent date that all goals were completed.
     * The primary use of this variable is to see if all goals were completed for the current day.
     * This way, if the user adds new goals during the current day, it is known if _daysGrown
     * should be decremented by seeing if the current day contributed to its value.
     *
     * i.e. User's adding goals when goals are completed means today is no longer a successful
     * growth day, so reset until all goals are completed again.
     */
    private var _lastDayGrown: MutableLiveData<LocalDate> =
        MutableLiveData<LocalDate>()
    internal val lastDayGrown: LiveData<LocalDate>
        get() = _lastDayGrown

    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        mainActivity.lifecycle.addObserver(this)
    }

    /*
     * Either new data needs to be set, or data created from previous sessions needs to be loaded.
     */
    internal fun loadData(mainActivity: MainActivity) {
        _plantFinished.postValue(false) // This is determined in main activity
        val userData = database.child("gardenData").child(userId)
        var lastDay = ""
        userData.child(R.string.days_grown.toString()).get().addOnSuccessListener {
            if (it.value != null){
                Log.i("loadData", "stored days grown = ${(it.value)}")
                _daysGrown.postValue((it.value as Long).toInt())
                Log.i("loadData", "updated days grown to ${_daysGrown.value}")
            } else {
                _daysGrown.postValue(1)
                Log.i("loadData", "stored days grown is null")
            }
        }.addOnFailureListener {
            _daysGrown.postValue(1)
            Log.i("loadData", "firebase error")
        }
        userData.child(R.string.last_day_grown.toString()).get().addOnSuccessListener {
            if (it.value != null && it.value != "null") {
                Log.i("loadData", "stored last day = ${(it.value)}")
                _lastDayGrown.postValue(LocalDate.parse(it.value as String))
            } else {
                _lastDayGrown.postValue(LocalDate.now().minusDays(1))
                Log.i("loadData", "stored last day is null")
            }
        }.addOnFailureListener {
            _lastDayGrown.postValue(LocalDate.now().minusDays(1))
            Log.i("loadData", "firebase error")
        }
    }

    internal fun loadData(sharedPrefs: SharedPreferences) {
        var isPrevData = false
        if (sharedPrefs.contains(R.string.days_grown.toString())) {
            isPrevData = true
        }

        if (!isPrevData) {
            _daysGrown.value = 1
            _plantFinished.value = false
            _lastDayGrown.value = LocalDate.now().minusDays(1)
            _plantsFinished.value = 0
        } else {
            _plantFinished.value = false // This is determined in main activity
            // load daysGrown
            _daysGrown.value = sharedPrefs.getInt(R.string.days_grown.toString(), 1)
            // load last day grown
            val lastDay = sharedPrefs.getString(R.string.last_day_grown.toString(), "01/01/1900")
            _lastDayGrown.value = LocalDate.parse(lastDay)
            _plantsFinished.value = sharedPrefs.getInt("plantsFinished", 0)
            Log.i("loadData", "got plantsFinished value as ${plantsFinished.value}")
        }
    }
    internal fun growthComplete(sharedPrefs: SharedPreferences) {
        _plantFinished.value = true
        _plantsFinished.value = _plantsFinished.value?.plus(1)
        _daysGrown.value = 1
        val editor = sharedPrefs.edit()
        editor.putInt(R.string.days_grown.toString(), _daysGrown.value!!)
        editor.putInt("plantsFinished", _plantsFinished.value!!)
        Log.i("growthComplete", "changed plantsFinished to ${_plantsFinished.value!!}")
        editor.apply()
    }

    internal fun resetPlantFinished() {
        _plantFinished.value = false
    }
    /*
    internal fun updateGrowthDay(growthDay: Boolean) {
        Log.i("updateGrowthDay", "_daysGrown = ${(_daysGrown.value)}")
        val userData = database.child("gardenData").child(userId)
        if (growthDay) {
            // increment days grown to include today
            _daysGrown.value = _daysGrown.value?.plus(1)
            // Mark today as the last day grown
            _lastDayGrown.value = LocalDate.now()
        } else {
            val currDate = LocalDate.now()
            // Today's goals might have been successfully completed, but a new goal was added.
            // In this case, today's goals need to all be completed again, so get rid of today's
            // successful growth
            if (_lastDayGrown.value?.dayOfYear == currDate.dayOfYear &&
                _lastDayGrown.value?.year == currDate.year) { // checks if goals were completed today
                // Set lastDayGrown as a date that isn't today (past is safe)
                _lastDayGrown.value = currDate.minusDays(1)
                // Decrement daysGrown
                _daysGrown.value = _daysGrown.value?.minus(1)
            }

        userData.child(R.string.days_grown.toString()).setValue(_daysGrown.value)
        Log.i("updateGrowthDay", "set value of days grown to ${(_daysGrown.value)}")
        userData.child(R.string.last_day_grown.toString()).setValue(_lastDayGrown.value.toString())
        Log.i("updateGrowthDay", "set value of last day grown to ${(_lastDayGrown.value)}")

        }


        //database.child("flowers").child(userId).child(flowerId).setValue(visible)

    }*/

    internal fun updateGrowthDay(growthDay: Boolean, sharedPrefs: SharedPreferences) {
        // Daily goals have been successfully completed
        if (growthDay) {
            // increment days grown to include today
            _daysGrown.value = _daysGrown.value?.plus(1)
            // Mark today as the last day grown
            _lastDayGrown.value = LocalDate.now()
        } else {
            val currDate = LocalDate.now()
            // Today's goals might have been successfully completed, but a new goal was added.
            // In this case, today's goals need to all be completed again, so get rid of today's
            // successful growth
            if (_lastDayGrown.value?.dayOfYear == currDate.dayOfYear &&
                    _lastDayGrown.value?.year == currDate.year) { // checks if goals were completed today
                // Set lastDayGrown as a date that isn't today (past is safe)
                _lastDayGrown.value = currDate.minusDays(1)
                // Decrement daysGrown
                _daysGrown.value = _daysGrown.value?.minus(1)
            }
        }
        val editor = sharedPrefs.edit()
        editor.putInt(R.string.days_grown.toString(), _daysGrown.value!!)
        editor.putString(R.string.last_day_grown.toString(),
            _lastDayGrown.value?.toString())
        editor.apply()
    }

    internal fun testDayComplete() {
        _lastDayGrown.value = LocalDate.now().minusDays(1)
    }
    internal fun removeFinishedPlants(sharedPrefs: SharedPreferences) {
        _plantsFinished.value = 0
        val editor = sharedPrefs.edit()
        editor.putInt("plantsFinished", 0)
        editor.apply()
        Log.i("removeFinishedPlants", "changed plantsFinished to 0")
    }
}