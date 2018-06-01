package com.event2go.base.utils;

import android.util.Log;

//import com.crashlytics.android.Crashlytics;

/**
 * Created by Iliya Gogolev on 6/5/15.
 */

/**
 * Created by cvoronin on 4/27/15.
 */
public class Logger {

    private static final String TAG = Logger.class.getSimpleName();

    private static final boolean FINAL_CONSTANT_IS_LOCAL = true;

    public static void e(String message) {
        loge(message);
    }

    public static void i(String message) {
        logi(message);
    }

    public static void d(String message) {
        logd(message);
    }

    /**
     * Private utils methods
     *
     * @param message
     */

    private static void loge(String message) {
//        if (Fabric.isInitialized()) {
//            Crashlytics.log(Log.ERROR, getLogTagWithMethod(), message);
//        } else {
            Log.e(getLogTagWithMethod(), message);
//        }

    }

    private static void logi(String message) {

//        if (Fabric.isInitialized()) {
//            Crashlytics.log(Log.INFO, getLogTagWithMethod(), message);
//        } else {
            Log.i(getLogTagWithMethod(), message);
//        }
    }

    private static void logd(String message) {
//        if (BuildConfig.DEBUG) {
            Log.d(getLogTagWithMethod(), message);
//        } else if (Fabric.isInitialized()) {
//            Crashlytics.log(Log.DEBUG, getLogTagWithMethod(), message);
//        }
    }

    private static String getLogTagWithMethod() {
        if (FINAL_CONSTANT_IS_LOCAL) {
            Throwable stack = new Throwable().fillInStackTrace();
            StackTraceElement trace = stack.getStackTrace()[3];
            return extractSimpleClassName(trace.getClassName()) + "." + trace.getMethodName() + ":" + trace.getLineNumber();
        } else {
            return TAG;
        }
    }

    public static String extractSimpleClassName(String fullClassName) {
        if ((null == fullClassName) || ("".equals(fullClassName)))
            return "";

        int lastDot = fullClassName.lastIndexOf('.');
        if (0 > lastDot)
            return fullClassName;

        return fullClassName.substring(++lastDot);
    }
}
