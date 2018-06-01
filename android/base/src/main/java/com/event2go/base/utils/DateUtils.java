package com.event2go.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Iliya Gogolev on 6/18/15.
 */
public class DateUtils {

    public static String getDayOfWeek(Date date) {

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);

        return getDayOfWeek(instance);
    }

    public static String getDayOfWeek(Calendar calendar) {

        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        return simpledateformat.format(calendar.getTime());
    }

    public static String getMonthName(Calendar calendar) {

        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        return month_date.format(calendar.getTime());
    }

    public static String getDateString(int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar =  DateUtils.getCalendarInstace();
        calendar.set(year, monthOfYear, dayOfMonth);

        String month_name = DateUtils.getMonthName(calendar);
        String dayOfWeek = DateUtils.getDayOfWeek(calendar);

        StringBuilder dateString = new StringBuilder()
                .append(dayOfWeek).append(", ").append(month_name)
                .append(" ").append(dayOfMonth).append(", ").append(year);

        return dateString.toString();

    }

    public static String getDateStringNoYear(Date date) {
        if (date == null) return "";

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return getDateStringNoYear(instance);
    }

    public static String getDateString(Date date) {
        if (date == null) return "";

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return getDateString(instance);
    }

    public static String getDateStringNoYear(Calendar calendar) {
        return getDateString(calendar, false);
    }

    public static String getDateString(Calendar calendar, boolean appepnYear) {
        if (calendar == null) return "";

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String month_name = DateUtils.getMonthName(calendar);
        String dayOfWeek = DateUtils.getDayOfWeek(calendar);

        StringBuilder dateString = new StringBuilder()
                .append(month_name)
                .append(" ").append(dayOfMonth);

        if (appepnYear) dateString.append(", ").append(year);

        return dateString.toString();
    }

    public static String getMonthDayString(Date date) {
        if (date == null) return "";

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return getDateString(instance);
    }

    public static String getMonthDayString(Calendar calendar) {
        if (calendar == null) return "";

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String month_name = DateUtils.getMonthName(calendar);

        StringBuilder dateString = new StringBuilder()
                .append(month_name)
                .append(" ").append(dayOfMonth);

        return dateString.toString();
    }

    public static String getDateString(Calendar calendar) {
        return getDateString(calendar, true);
    }

    public static String getTimeString(Date date) {
        if (date == null) return "";

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return getTimeString(instance);
    }

    public static String getTimeString(Calendar calendar) {

        if (calendar == null) return "";

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return getTime(hours, minutes);
    }

    public static String getTime(int hourOfDay, int minute) {

        return new StringBuffer().append(padding_str(hourOfDay))
                .append(":")
                .append(padding_str(minute))
                .toString();

    }

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static Calendar getCalendarInstace(){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        return calendar;

    }

    public static String getNotificationTime(Date date) {
        SimpleDateFormat notificationDate = new SimpleDateFormat("d MMM HH:mm");
        return notificationDate.format(date);
    }

    public static Date parseDateFromJSonString(String time) {
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = format.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getChatMessageTime(Date date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MM:hh");
        return simpledateformat.format(date.getTime());
    }
}
