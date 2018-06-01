package com.event2go.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.Log;

import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.event2go.app.features.event.data.PartStatType;

/**
 * Created by Iliya Gogolev on 12/18/15.
 */
public class UIUtils {

    public static Drawable getEventPartStatResId(String partstat) {


        // workaround ic_thumbs_up_down_black_24dp_copy, doesn't work when 1st even tentative,
        // second action needed
        @DrawableRes int resId = R.drawable.ic_thumbs_up_down_black_24dp_copy;
        @ColorRes int colorId = R.color.partstat_action_required;

        if (TextUtils.isEmpty(partstat)) {
            colorId = R.color.partstat_maybe;
            resId = R.drawable.ic_thumbs_up_down_black_24dp;
        }else if (partstat.equalsIgnoreCase(PartStatType.ACCEPTED)) {
            resId = R.drawable.ic_thumb_up_black_24dp;
            colorId = R.color.partstat_going;
        } else if (partstat.equalsIgnoreCase(PartStatType.DECLINED)) {
            resId = R.drawable.ic_thumb_down_black_24dp;
            colorId = R.color.partstat_not_going;
        } else if (partstat.equalsIgnoreCase(PartStatType.TENTATIVE)) {
            colorId = R.color.partstat_maybe;
            resId = R.drawable.ic_thumbs_up_down_black_24dp;
        }

        Log.d("AA", partstat + " " + colorId);


        final Context context = AppApplication.getContext();
        Drawable drawable = DrawableCompat.wrap(
                ResourcesCompat.getDrawable(context.getResources(), resId, null)
        );

        drawable = DrawableCompat.wrap(drawable);
//        DrawableCompat.setTint(
//                drawable,
//                ContextCompat.getColor(listener, colorId)
//        );

        Handler handler = new Handler(Looper.myLooper());
        final Drawable finalDrawable = drawable;
        final int finalColorId = colorId;
        handler.post(new Runnable() {
            @Override
            public void run() {
                DrawableCompat.setTint(
                        finalDrawable,
                        ContextCompat.getColor(context, finalColorId)
                );
            }
        });

        return drawable;
    }
}
