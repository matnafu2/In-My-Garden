package com.example.inmygarden

import androidx.lifecycle.*
import java.time.LocalDate
import java.time.Period
import java.util.*

class GardenViewModel : ViewModel(), DefaultLifecycleObserver {

    // LocalDate instance is used to keep track of the number of days passed in plant growth cycle
    private val _firstDay: MutableLiveData<LocalDate> =
        MutableLiveData<LocalDate>()
    internal val firstDay: LiveData<LocalDate>
        get() = _firstDay

    /*
     * daysGrown live data keeps track of the stage of growth that the flower is in out of 7 stages
     * (one week). This will allow for the proper stage of growth to be displayed, and for
     * starting a new plant.
     */
    private val _daysGrown: MutableLiveData<Int> =
        MutableLiveData<Int>()
    internal val daysGrown: LiveData<Int>
        get() = _daysGrown

    private val _plantFinished: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>(false)
    internal val plantFinished: LiveData<Boolean>
        get() = _plantFinished

    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        mainActivity.lifecycle.addObserver(this)
    }

    internal fun setDefaultDays() {
        if (true) {
            _daysGrown.value = 0
            _firstDay.value = LocalDate.now()
        } else {
            // load first day
            _firstDay.value = LocalDate.of(2022, 12, 1)
            // calculate days grown
            val currDate = LocalDate.now()
            var period = Period.between(_firstDay.value, currDate)
            _daysGrown.value = period.days
        }
    }

    internal fun growthComplete() {
        _plantFinished.value = true
    }
}