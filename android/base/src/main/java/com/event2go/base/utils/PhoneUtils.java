package com.event2go.base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by Iliya Gogolev on 4/1/16.
 */
public class PhoneUtils {

    @SuppressLint("MissingPermission")
    public static String getDevicePhoneNumber(Context context, PermissionUtils.OnPermissionRequired listener, int requestCode) {

        String phoneNumber = null;
        if (new PermissionUtils(listener).check(requestCode, new String[]{Manifest.permission.READ_PHONE_NUMBERS})) {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            phoneNumber = tMgr.getLine1Number();
            if (!phoneNumber.startsWith("+")) {
                phoneNumber = "+" + phoneNumber;
            }

            phoneNumber = convertToValidPhoneNumber(phoneNumber, true);
        }
        return phoneNumber;
    }

    public static String convertToValidPhoneNumber(String phoneNumber, boolean removeCountryCode) {
        try {
            Phonenumber.PhoneNumber phoneNumber1 = PhoneNumberUtil.getInstance().parse(phoneNumber, "");
            if (removeCountryCode) {
                return "" + phoneNumber1.getNationalNumber();
            }
            return "+" + phoneNumber1.getCountryCode() + phoneNumber1.getNationalNumber();
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return phoneNumber;

    }

    public static Set<String> getSupportedRegions() {
        return PhoneNumberUtil.getInstance().getSupportedRegions();
    }

    public static boolean isPhoneValid1(String number) {
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = PhoneNumberUtil.getInstance().parse(number, "");
            return PhoneNumberUtil.getInstance().isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isPhoneValid(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        } else if (number.length() < 6) {
            return false;
        }

        return android.util.Patterns.PHONE.matcher(number).matches();
    }

}
