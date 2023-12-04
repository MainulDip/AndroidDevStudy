package com.websolverpro.service_2_generalservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.websolverpro.service_2_generalservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        binding.apply {
            btnStart.setOnClickListener {
                Log.d("First Button Call", "onCreate: Start btn clicked")
                Intent(this@MainActivity, MyService::class.java).also {
                    startService(it)
                    ftext.text = "MyService Running"
                }
            }

            btnEnd.setOnClickListener {
                Log.d("Second Button Call", "onCreate: End btn clicked")
                Intent(this@MainActivity, MyService::class.java).also {
                    stopService(it)
                    ftext.text = "MyService Stopped"
                }
            }

            sendDataBtn.setOnClickListener {
                Intent(this@MainActivity, MyService::class.java).also {
                    val dataString = this.dataString.text?.toString()
                    it.putExtra("EXTRA_DATA", dataString)
                    startService(it)
                    ftext.text = "MyService Started With Custom Data"
                }
            }
        }
    }
}