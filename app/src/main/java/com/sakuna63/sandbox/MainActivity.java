package com.sakuna63.sandbox;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sakuna63.sandbox.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.image1.setOnClickListener(this);
        binding.image2.setOnClickListener(this);
        binding.image3.setOnClickListener(this);
        binding.image4.setOnClickListener(this);
        binding.image5.setOnClickListener(this);
        binding.image6.setOnClickListener(this);
        binding.image7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        Log.d("MainActivity", "xy:" + Arrays.toString(xy));
        Log.d("MainActivity", "v.getLeft():" + v.getLeft());
        Log.d("MainActivity", "v.getTop():" + v.getTop());
        Log.d("MainActivity", "v.getX():" + v.getX());
        Log.d("MainActivity", "v.getY():" + v.getY());
    }
}
