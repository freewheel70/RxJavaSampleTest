package com.hong.app.rxjavatest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Freewheel on 2016/4/17.
 */
public class DateUtil {


    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long ONE_DAY = ONE_HOUR * 24;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");


    public static String getDisplayDateString(Date date) {
        Date curDate = new Date();
        long elapsedTime = curDate.getTime() - date.getTime();
        if (elapsedTime < (30 * ONE_DAY)) {
            if (elapsedTime < ONE_MINUTE) {
                return "刚刚";
            }
            if (elapsedTime < ONE_HOUR) {
                return String.format("%d分钟前", elapsedTime / ONE_MINUTE);
            }

            if (elapsedTime < ONE_DAY) {
                return String.format("%d小时前", elapsedTime / ONE_HOUR);
            }

            return String.format("%d天前", elapsedTime / ONE_DAY);
        }
        String result;
        result = "M月d日 HH:mm";
        return (new SimpleDateFormat(result, Locale.CHINA)).format(date);
    }

    public static String toServerAcceptableDateString(Date date) {
        return dateFormat.format(date);
    }

    public static Date toServerDate(String dateStr) throws ParseException {
        return dateFormat.parse(dateStr);
    }

}
