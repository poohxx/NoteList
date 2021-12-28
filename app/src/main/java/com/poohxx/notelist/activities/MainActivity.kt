package com.poohxx.notelist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBotNavViewListener()
    }
    private fun setBotNavViewListener(){
        binding.apply {
            btmNavView.setOnItemReselectedListener {
                when (it.itemId){
                    R.id.settings -> {
                        Toast.makeText(this@MainActivity, "settings", Toast.LENGTH_SHORT).show()
                    }
                    R.id.new_task -> {Log.d("Log", "new tasl")}
                    R.id.task_list -> {Log.d("Log", "task_list")}
                    R.id.notes -> {Log.d("Log", "notes")}
                }
                true
            }
        }
    }
}