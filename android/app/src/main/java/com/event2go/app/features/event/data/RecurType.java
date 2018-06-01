package com.event2go.app.features.event.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Iliya Gogolev on 1/22/16.
 */
public class RecurType {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, HOURLY, DAILY, WEEKLY, MONTHLY, COUNT})
    public @interface Recurrency {
    }

    public static final int NONE = 0;
    public static final int HOURLY = 1;
    public static final int DAILY = 2;
    public static final int WEEKLY = 3;
    public static final int MONTHLY = 4;
    public static final int COUNT = 5;
}
