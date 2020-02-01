package com.jh.automatic_titrator.common.utils;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.jh.automatic_titrator.common.Cache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 2016/10/27.
 */
public class TimeTool {

    public static String dateToDate(int date, int time) {
        Date date1 = new Date((long) date * 3600 * 1000 * 24 + time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date1);
    }

    public static String timeToTime(int date, int time) {
        Date date1 = new Date((long) date * 3600 * 1000 * 24 + time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date1);
    }

    public static String dateFormatter(long time, String templete) {
        Date date1 = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(templete);
        return simpleDateFormat.format(date1);
    }

    public static String dateToDate(String date) {
        return date.split(" ")[0];
    }

    public static String dateToTime(String date) {
        String[] splits = date.split(" ", 2);
        if (splits.length > 1) {
            return splits[1];
        } else {
            return "00:00:00";
        }
    }

    public static String dateToMinite(String date) {
        String[] splits = date.split(" ", 2);
        if (splits.length > 1) {
            return splits[1].substring(0, splits[1].lastIndexOf(":"));
        } else {
            return "00:00";
        }
    }

    public static String dateToMinite(long date) {
        Date date1 = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date1);
    }

    public static String currentDate() {
        Date date = new Date(Cache.currentTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String getDate(DatePicker datePicker, boolean isStart) {
        StringBuilder sb = new StringBuilder();
        sb.append(datePicker.getYear());
        int month = datePicker.getMonth() + 1;
        if (month < 10) {
            sb.append("-0").append(month);
        } else {
            sb.append("-").append(month);
        }
        int day = datePicker.getDayOfMonth();
        if (day < 10) {
            sb.append("-0").append(day);
        } else {
            sb.append("-").append(day);
        }
        if (isStart) {
            sb.append(" 00:00:00");
        } else {
            sb.append(" 24:00:00");
        }
        return sb.toString();
    }

    public static String getTime(TimePicker timePicker, boolean isStartDate) {
        StringBuilder sb = new StringBuilder();
        int hour = timePicker.getCurrentHour();
        int minite = timePicker.getCurrentMinute();
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(hour).append(":");
        if (minite < 10) {
            sb.append("0");
        }
        sb.append(minite);
        return sb.toString();
    }

    public static long getTimeOffset(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            long t1 = sf.parse(time).getTime();
            return t1 - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getTime(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static String getTimeedId() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(date) + date.getTime() % 1000;
    }
}
