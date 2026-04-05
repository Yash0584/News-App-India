package com.example.indianews.utils;

import android.text.format.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatUtils {
    
    /**
     * Format NewsData.io date string to relative time (e.g., "2 hours ago")
     * NewsData.io format: "2026-03-29 10:30:00"
     */
    public static String getTimeAgo(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            
            if (date != null) {
                long now = System.currentTimeMillis();
                long time = date.getTime();
                
                return DateUtils.getRelativeTimeSpanString(
                        time,
                        now,
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                ).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return dateString;
    }
    
    /**
     * Format date to readable format (e.g., "March 29, 2026")
     */
    public static String getReadableDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(dateString);
            
            if (date != null) {
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return dateString;
    }
    
    /**
     * Format date to full format with time (e.g., "March 29, 2026 at 10:30 AM")
     */
    public static String getFullDateTime(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(dateString);
            
            if (date != null) {
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return dateString;
    }
}
