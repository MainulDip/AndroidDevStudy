package com.example.busschedule.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.busschedule.database.schedule.Schedule
import com.example.busschedule.database.schedule.ScheduleDao

class BusScheduleViewModel(private val scheduleDao: ScheduleDao) : ViewModel() {

    // get the full bus schedule from scheduleDao param
    fun fullSchedule(): List<Schedule> = scheduleDao.getAll()

    // get the arrival time
    fun scheduleForStopName(name: String): List<Schedule> = scheduleDao.getByStopName(name)
}

/*
* Create Factory class to instantiate the BusScheduleViewModel
* As BusScheduleViewModel should be instantiated by an object that can respond to lifecycle events
* ViewModelProvider can be used as it is a lifecycle aware class
*/

class BusScheduleViewModelFactory(private val scheduleDao: ScheduleDao) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BusScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusScheduleViewModel(scheduleDao) as T
        }
        throw IllegalAccessException("Something wrong with the custom BusScheduleViewModel class")
    }
}