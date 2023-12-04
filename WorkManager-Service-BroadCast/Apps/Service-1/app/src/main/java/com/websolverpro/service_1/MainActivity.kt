package com.websolverpro.service_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.websolverpro.service_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            btnStart.setOnClickListener {
                Log.d("First Button Call", "onCreate: Start btn clicked")
            }

            btnEnd.setOnClickListener {
                Log.d("Second Button Call", "onCreate: End btn clicked")
            }
        }

    }
}