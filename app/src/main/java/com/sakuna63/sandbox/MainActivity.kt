package com.sakuna63.sandbox

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sakuna63.sandbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, savedInstanceState.toString(), Toast.LENGTH_SHORT).show()

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.text.text = "Main Activity!!!"
        binding.text.setOnClickListener {
            SharedCache.cache.put("hogehoge", listOf(
                    "hoge", "fuga", "bar"
            ))

            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, savedInstanceState.toString(), Toast.LENGTH_SHORT).show()
        Toast.makeText(this, savedInstanceState?.getStringArrayList("saved").toString(), Toast.LENGTH_SHORT).show()

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        binding.text.text = "Second Activity!!!"
//        Log.d("TAG", SharedCache.cache.toString())
        binding.text.text = SharedCache.cache.toString()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        SharedCache.cache["hogehoge"]?.let {
            outState?.putStringArrayList("saved", arrayListOf(*it.toTypedArray()))
        }
    }
}

object SharedCache {
    val cache = mutableMapOf<String, List<String>>()
}
