package com.sakuna63.sandbox;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sakuna63.sandbox.databinding.ActivityMainBinding;

import org.parceler.Parcel;
import org.parceler.ParcelConverter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Parcel(converter = DummyConverter.class)
    interface Interface {

    }

    @Parcel
    static class Data {
        public List<? extends Interface> items;
    }

    static class DummyConverter implements ParcelConverter<Interface> {

        @Override
        public void toParcel(Interface input, android.os.Parcel parcel) {

        }

        @Override
        public Interface fromParcel(android.os.Parcel parcel) {
            return null;
        }
    }

}
