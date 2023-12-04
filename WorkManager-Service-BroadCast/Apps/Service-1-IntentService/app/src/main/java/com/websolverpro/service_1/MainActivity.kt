package com.websolverpro.service_1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import com.websolverpro.service_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Without View binding
         */
//        setContentView(R.layout.activity_main)
//        val btnStart = findViewById<Button>(R.id.btnStart)
//        btnStart.setOnClickListener {
//                Log.d("First Button Call", "onCreate: Start btn clicked")
//        }

        /**
         * Using View binding
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            btnStart.setOnClickListener {
                Log.d("First Button Call", "onCreate: Start btn clicked")
                Intent(this@MainActivity, MyIntentService::class.java).also {
                    startService(it)
                    ftext.text = "Service Started"
                }
            }

            btnEnd.setOnClickListener {
                Log.d("Second Button Call", "onCreate: End btn clicked")
                MyIntentService.stopServiceCustom()
                ftext.text = "Service Stopped"
            }
        }

    }
}