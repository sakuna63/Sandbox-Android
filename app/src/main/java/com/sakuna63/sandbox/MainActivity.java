package com.sakuna63.sandbox;

import com.sakuna63.sandbox.databinding.ActivityMainBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat i) {
                Log.d("MainActivity", "i.getStableInsetTop():" + i.getStableInsetTop());
                Log.d("MainActivity", "i.getStableInsetBottom():" + i.getStableInsetBottom());
                Log.d("MainActivity", "i.getStableInsetLeft():" + i.getStableInsetLeft());
                Log.d("MainActivity", "i.getStableInsetRight():" + i.getStableInsetRight());
                Log.d("MainActivity", "i.getSystemWindowInsetTop():" + i.getSystemWindowInsetTop());
                Log.d("MainActivity", "i.getSystemWindowInsetBottom():" + i.getSystemWindowInsetBottom());
                Log.d("MainActivity", "i.getSystemWindowInsetLeft():" + i.getSystemWindowInsetLeft());
                Log.d("MainActivity", "i.getSystemWindowInsetRight():" + i.getSystemWindowInsetRight());

                return i;
            }
        });

//        binding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WindowInsets i2 = binding.getRoot().getRootWindowInsets();
//                Log.d("MainActivity", "i2.getStableInsetTop():" + i2.getStableInsetTop());
//                Log.d("MainActivity", "i2.getStableInsetBottom():" + i2.getStableInsetBottom());
//                Log.d("MainActivity", "i2.getStableInsetLeft():" + i2.getStableInsetLeft());
//                Log.d("MainActivity", "i2.getStableInsetRight():" + i2.getStableInsetRight());
//                Log.d("MainActivity", "i2.getSystemWindowInsetTop():" + i2.getSystemWindowInsetTop());
//                Log.d("MainActivity", "i2.getSystemWindowInsetBottom():" + i2.getSystemWindowInsetBottom());
//                Log.d("MainActivity", "i2.getSystemWindowInsetLeft():" + i2.getSystemWindowInsetLeft());
//                Log.d("MainActivity", "i2.getSystemWindowInsetRight():" + i2.getSystemWindowInsetRight());
//            }
//        });
    }
}
