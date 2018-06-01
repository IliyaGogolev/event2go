package com.event2go.base.utils;

import android.os.Build;

/**
 * Created by Iliya Gogolev on 3/7/16.
 */
public class DeviceUtils {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }
}
