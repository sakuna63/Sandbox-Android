package com.sakuna63.sandbox;

import com.sakuna63.sandbox.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://www.wantedly.com/users/4318497/avatar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("MainActivity", "onBitmapLoaded() called with: bitmap = [" + bitmap + "], from = [" + from + "]");
                binding.image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(this)
                .load(URL)
                .into(target);

//        binding.image.setColorFilter(Color.parseColor("#330000ff"));
    }
}
