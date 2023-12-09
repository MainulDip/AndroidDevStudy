package com.websolverpro.roommigration_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "user.db"
        ).addMigrations(UserDatabase.migration3To4).build()

        lifecycleScope.launch {
//            db.dao.getUsers().forEach(::println)
            db.dao.getUsers().forEach {
                Log.d("Room-User", "$it")
            }

            db.dao.getSchools().forEach {
                Log.d("Room-School", "$it")
            }
        }


        println(db)

        /**
         * Inserting User
         * This Block can be commented after first run
         */
//        (10..20).forEach {
//
//            lifecycleScope.launch {
//                db.dao.insertUser(
//                    User(
//                        email = "test@test$it.com",
//                        userName = "test$it"
//                    )
//                )
//            }
//        }

        /**
         * Inserting Schools
         * This Block can be commented after first run
         */
//        (1..10).forEach {
//
//            lifecycleScope.launch {
//                db.dao.insertSchool(
//                    School("School$it")
//                )
//            }
//        }

        /**
         * Deleting the database
         * Sometime while on development, its easier to delete the db
         * to avoid Auto Migration and Manual Migration Errors
         */
//        deleteDatabase("user.db")

    }
}