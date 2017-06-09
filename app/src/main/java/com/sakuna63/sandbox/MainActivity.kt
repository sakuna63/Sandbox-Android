package com.sakuna63.sandbox

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sakuna63.sandbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.buttonA.setOnClickListener { showTab("A") }
        binding.buttonB.setOnClickListener { showTab("B") }
        binding.buttonC.setOnClickListener { showTab("C") }
        binding.buttonD.setOnClickListener { showTab("D") }

        binding.hideButtonA.setOnClickListener { hideTab("A") }
        binding.hideButtonB.setOnClickListener { hideTab("B") }
        binding.hideButtonC.setOnClickListener { hideTab("C") }
        binding.hideButtonD.setOnClickListener { hideTab("D") }

        binding.addButtonA.setOnClickListener { addTab("A") }
        binding.addButtonB.setOnClickListener { addTab("B") }
        binding.addButtonC.setOnClickListener { addTab("C") }
        binding.addButtonD.setOnClickListener { addTab("D") }
    }

    fun showTab(s: String) {
        val fragment = supportFragmentManager.findFragmentByTag(s)
        supportFragmentManager.beginTransaction()
                .show(fragment)
                .commitNow()
    }

    fun hideTab(s: String) {
        val fragment = supportFragmentManager.findFragmentByTag(s)
        supportFragmentManager.beginTransaction()
                .hide(fragment)
                .commitNow()
    }

    private fun addTab(s: String) {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, MyFragment().also {
                    it.arguments = Bundle().also {
                        it.putString("key", s)
                    }
                }, s)
                .commitNow()
    }
}
