package com.example.stopwatch

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var isRunning = false
    private var minutes: String? = "00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val lapsList = ArrayList<String>()
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lapsList)
        binding.listView.adapter = arrayAdapter

        binding.lap.setOnClickListener {
            if (isRunning) {
                lapsList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        binding.clock.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog)
            val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 5

            dialog.findViewById<Button>(R.id.set_time).setOnClickListener {
                minutes = numberPicker.value.toString()
                binding.clocktime.text = "${minutes} mins"
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.run.setOnClickListener {
            if (!isRunning) {
                if (minutes != null && minutes != "00") {
                    val totalMin = minutes!!.toInt() * 60 * 1000L
                    binding.chronometer.base = SystemClock.elapsedRealtime() + totalMin
                    binding.chronometer.format = "%S"
                    binding.chronometer.start()

                    binding.chronometer.setOnChronometerTickListener {
                        val elapsedTime = SystemClock.elapsedRealtime() - binding.chronometer.base
                        if (elapsedTime >= totalMin) {
                            binding.chronometer.stop()
                            isRunning = false
                            binding.run.text = "RUN"
                        }
                    }
                }
                binding.run.text = "STOP"
                isRunning = true
            } else {
                binding.chronometer.stop()
                isRunning = false
                binding.run.text = "RUN"
            }
        }
    }
}
