package com.example.clockapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static Date getAdjustedTime() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.add(Calendar.MINUTE, currentHour);
        return calendar.getTime();
    }

    public static String formatTime(Date date) {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(date);
    }
}
