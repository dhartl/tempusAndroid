package at.c02.tempus.app.ui.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 12.04.2017.
 */

public class DateUtils {

    public static DateFormat getDateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }

    public static DateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm");
    }

    public static DateFormat getDateTimeFormat() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm");
    }

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date setDate(Date date, int year, int month, int day) {
        Calendar calendar;
        if (date == null) {
            calendar = getZeroCalendar();
        } else {
            calendar = dateToCalendar(date);
        }
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static Date setTime(Date date, int hours, int minutes) {
        Calendar calendar;
        if (date == null) {
            calendar = getZeroCalendar();
        } else {
            calendar = dateToCalendar(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Calendar getZeroCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
