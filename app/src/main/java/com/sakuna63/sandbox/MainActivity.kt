package com.sakuna63.sandbox

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.sakuna63.sandbox.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.root.setOnClickListener {
            Log.d("TAG", "click")
            thread {
                Log.d("TAG", "in new thread")
                if (Looper.myLooper() == null) {
                    Looper.prepare()
                }
                binding.root.setBackgroundColor(Color.BLUE)
//                AlertDialog.Builder(this@MainActivity)
//                        .setTitle("test")
//                        .setMessage("test test test")
//                        .show()
                Log.d("TAG", "finish")
            }
        }
    }
}
