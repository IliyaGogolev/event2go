package com.event2go.app.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.event2go.app.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This utility class provides custom bindings to widgets defined in XML layouts allowing the
 * use of custom attributes on an element.
 * <p/>
 * Created by Iliya Gogolev on 7/30/15.
 */
public class BindingAdapters {

    @BindingAdapter({"bind:imageUrl"})
    public static void setImageUrl(ImageView view, String url) {
        if (url != null) {
            Glide.with(view.getContext()).load(url).into(view);
        }
    }

    @BindingAdapter({"bind:imageUrl", "bind:placeHolder"})
    public static void setImageUrlWithHolder(ImageView view, String url, Drawable placeHolder) {
        Glide.with(view.getContext()).load(url).apply(
                new RequestOptions().placeholder(placeHolder)
        ).into(view);
    }


    @BindingAdapter({"bind:uri"})
    public static void loadImage(CircleImageView view, Uri uri) {
        Glide.with(view.getContext()).load(uri).into(view);
    }

    @BindingAdapter({"bind:imageUrl", "bind:placeHolder"})
    public static void loadImage(CircleImageView view, String uri, Drawable placeHolder) {
        Glide.with(view.getContext()).load(uri).apply(
                new RequestOptions().placeholder(placeHolder)
        ).into(view);
    }

    @BindingAdapter({"bind:stringResId"})
    public static void setTextById(TextView textView, @StringRes int resId) {
        // In some cases the listener may be inflated before binding occurs,
        // in those cases the resId is 0 and we guard here to prevent an exception
        if (resId != 0) {
            textView.setText(resId);
        }
    }

//    android:text="@{DateUtils.getRelativeTimeSpanString(notification.createdAt.getTime())}"

    @BindingAdapter({"bind:charSequence"})
    public static void setTextCharSequenced(TextView textView, CharSequence charSequence) {
        // In some cases the listener may be inflated before binding occurs,
        // in those cases the resId is 0 and we guard here to prevent an exception
        textView.setText(charSequence, TextView.BufferType.SPANNABLE);
    }

    @BindingAdapter({"bind:strings", "bind:selectedPosition"})
    public static void setSpinnerStringsItems(AppCompatSpinner spinner, String[] items, int selectedPosition) {
//        new ArrayAdapter<String>(spinner.getListener(), R.layout.list_item_spinner_item, items));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), R.layout.list_item_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.list_item_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (selectedPosition >= 0) spinner.setSelection(selectedPosition);
    }
}
