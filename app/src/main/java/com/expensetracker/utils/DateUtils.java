package com.expensetracker.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

    public static String formatDateTime(long timestamp) {
        return dateTimeFormat.format(new Date(timestamp));
    }

    public static String formatDate(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    public static String formatTime(long timestamp) {
        return timeFormat.format(new Date(timestamp));
    }

    public static long[] getCurrentMonthRange() {
        Calendar calendar = Calendar.getInstance();
        
        // Start of month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfMonth = calendar.getTimeInMillis();
        
        // End of month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        long endOfMonth = calendar.getTimeInMillis();
        
        return new long[]{startOfMonth, endOfMonth};
    }

    public static long[] getMonthRange(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        
        // Start of month
        calendar.set(year, month, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfMonth = calendar.getTimeInMillis();
        
        // End of month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        long endOfMonth = calendar.getTimeInMillis();
        
        return new long[]{startOfMonth, endOfMonth};
    }

    public static String getCurrentMonthYear() {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return monthYearFormat.format(new Date());
    }

    public static String getMonthYearFromCalendar(Calendar calendar) {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return monthYearFormat.format(calendar.getTime());
    }
}