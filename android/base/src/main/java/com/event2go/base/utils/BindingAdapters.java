package com.event2go.base.utils;

import android.databinding.BindingAdapter;
import android.support.annotation.StringRes;
import android.widget.TextView;

/**
 * This utility class provides custom bindings to widgets defined in XML layouts allowing the
 * use of custom attributes on an element.
 * <p/>
 * Created by Iliya Gogolev on 7/30/15.
 */
public class BindingAdapters {

    @BindingAdapter({"bind:stringResId"})
    public static void setTextById(TextView textView, @StringRes int resId) {
        if (resId != 0) {
            textView.setText(resId);
        }
    }

}
