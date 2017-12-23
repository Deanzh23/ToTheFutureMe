package com.dean.tothefutureme.utils;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类
 * <p>
 * Created by dean on 2017/12/5.
 */
public class DateTimeUtils {

    /**
     * 日期格式
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    /**
     * 日期时间格式
     */
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);

    /**
     * 获取指定日期的毫秒数形式值
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static long getDateMillisecond(String date) throws ParseException {
        String longString = String.valueOf(DATE_FORMAT.parse(date).getTime());
        return Long.valueOf(longString.substring(0, longString.length() - 3));
    }

    /**
     * 获取指定日期时间的毫秒值对应的字符串形式值
     *
     * @param dateTime
     * @return
     */
    public static String getDateTimeMillisecond2String(long dateTime) {
        return DATE_TIME_FORMAT.format(new Date(dateTime));
    }

    /**
     * 获取指定日期的日期型字符串
     *
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * xml中long转日期字符串
     * <p>
     * app:text_long2DateString="@{long值}"
     *
     * @param textView
     * @param timeLong
     */
    @BindingAdapter({"text_long2DateString"})
    public static void long2DateString(TextView textView, long timeLong) {
        textView.setText(DATE_FORMAT.format(new Date(timeLong)));
    }

    /**
     * xml中long转日期字符串
     * <p>
     * app:text_long2DateTimeString="@{long值}"
     *
     * @param textView
     * @param timeLong
     */
    @BindingAdapter({"text_long2DateTimeString"})
    public static void long2DateTimeString(TextView textView, long timeLong) {
        textView.setText(DATE_TIME_FORMAT.format(new Date(timeLong)));
    }

}
