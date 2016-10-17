package com.sakuna63.sandbox;

import android.databinding.BindingAdapter;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataBindingAdapters {

    @BindingAdapter("marginVertical")
    public static void setMarginVertical(TextView textView, int dp) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        params.topMargin = dp;
        params.bottomMargin = dp;
    }
}
