package com.event2go.app.features.event.data;

import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.event2go.app.features.event.data.RecurType;
import com.event2go.base.utils.DateUtils;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Iliya Gogolev on 10/31/15.
 */
public class RecurUtils {

//    Examples
//    http://www.grokkingandroid.com/recurrence-rule-and-duration-formats/

    /***********************
     * UTILS
     **********************/

    public static SortedMap<Integer, String> getAlertDurUnitNames(boolean isOneUnit) {

        AppApplication context = AppApplication.getContext();
        SortedMap<Integer, String> result = new TreeMap<>();

        String durValue = "";
        for (int i = 0; i < RecurType.COUNT; i++) {

            switch (i) {
                case RecurType.NONE:
                    break;
                case RecurType.HOURLY:
                    durValue = isOneUnit ? context.getString(R.string.hour) : context.getString(R.string.hours);
                    result.put(RecurType.HOURLY, durValue);
                    break;
                case RecurType.DAILY:
                    durValue = isOneUnit ? context.getString(R.string.day) : context.getString(R.string.days);
                    result.put(RecurType.DAILY, durValue);
                    break;
                case RecurType.WEEKLY:
                    durValue = isOneUnit ? context.getString(R.string.week) : context.getString(R.string.weeks);
                    result.put(RecurType.WEEKLY, durValue);
                    break;
            }
        }

        return result; // Arrays.asList(listener.getResources().getStringArray(R.array.event_confirm_presence_dur_units_array));
    }


    public static String getFrequencyText(@RecurType.Recurrency int frequency, Date recurDateEnd) {
        AppApplication context = AppApplication.getContext();

        StringBuffer stringBuffer = new StringBuffer();
        switch (frequency) {
            case RecurType.NONE:
                stringBuffer.append(context.getString(R.string.event_repeat_non));
                break;
            case RecurType.DAILY:
                stringBuffer.append(context.getString(R.string.event_repeat_daily));
                break;
            case RecurType.WEEKLY:
                stringBuffer.append(context.getString(R.string.event_repeat_weekly));
                if (recurDateEnd != null) {
                    stringBuffer.append(" ");
                    stringBuffer.append(context.getString(R.string.on));
                    stringBuffer.append(DateUtils.getDayOfWeek(recurDateEnd));
                }
                break;
            case RecurType.MONTHLY:
                stringBuffer.append(context.getString(R.string.event_repeat_monthly));
                break;
        }

        if (frequency != RecurType.NONE && recurDateEnd != null) {
            stringBuffer.append(", ");
            stringBuffer.append(context.getString(R.string.until));
            stringBuffer.append(" ");
            stringBuffer.append(DateUtils.getMonthDayString(recurDateEnd));
        }

        return stringBuffer.toString();
    }
}

