package com.example.busschedule.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.busschedule.database.schedule.ScheduleDao

abstract class AppDatabase: RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    // Singleton
    companion object {

        /*
        * Marks the JVM backing field of the annotated property as volatile,
        * meaning that writes to this field are immediately made visible to other threads
        */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // synchronized(object) means, Executes the given function block while holding the monitor of the given object lock.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}