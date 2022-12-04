package com.example.inmygarden

import androidx.lifecycle.*
import java.time.LocalDate
import java.util.*

class GardenViewModel : ViewModel(), DefaultLifecycleObserver {

    /*
     * daysGrown live data keeps track of the stage of growth that the flower is in out of 7 stages
     * (one week). This will allow for the proper stage of growth to be displayed, and for
     * knowing when to start a new plant.
     */
    private val _daysGrown: MutableLiveData<Int> =
        MutableLiveData<Int>()
    internal val daysGrown: LiveData<Int>
        get() = _daysGrown

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
    private lateinit var lastDayGrown: LocalDate

    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        mainActivity.lifecycle.addObserver(this)
    }

    internal fun setDefaultDays() {
        if (true) {
            _daysGrown.value = 0
            _plantFinished.value = false
            lastDayGrown = LocalDate.now().minusDays(1)
        } else {
            // load daysGrown

            // load last day grown

        }
    }

    internal fun growthComplete() {
        _plantFinished.value = true
    }

    internal fun updateDate() {

    }

    internal fun updateGrowthDay(growthDay: Boolean) {
        // Daily goals have been successfully completed
        if (growthDay) {
            // increment days grown to include today
            _daysGrown.value = _daysGrown.value?.plus(1)
            // Mark today as the last day grown
            lastDayGrown = LocalDate.now()
        } else {
            val currDate = LocalDate.now()
            // Today's goals might have been successfully completed, but a new goal was added.
            // In this case, today's goals need to all be completed again, so get rid of today's
            // successful growth
            if (lastDayGrown.dayOfYear == currDate.dayOfYear &&
                    lastDayGrown.year == currDate.year) { // checks if goals were completed today
                // Set lastDayGrown as a date that isn't today (past is safe)
                lastDayGrown = currDate.minusDays(1)
                // Decrement daysGrown
                _daysGrown.value = _daysGrown.value?.minus(1)
            }
        }
    }
}