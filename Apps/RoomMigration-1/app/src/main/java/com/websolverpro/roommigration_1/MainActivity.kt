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
        ).build()

        lifecycleScope.launch {
//            db.dao.getUsers().forEach(::println)
            db.dao.getUsers().forEach {
                Log.d("Room-User", "$it")
            }
        }


        println(db)

        (10..20).forEach {

            lifecycleScope.launch {
                db.dao.insertUser(
                    User(
                        email = "test@test$it.com",
                        userName = "test$it"
                    )
                )
            }
        }

//        deleteDatabase("user.db")

    }
}