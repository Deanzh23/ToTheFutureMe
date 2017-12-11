package com.dean.tothefutureme.utils;

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
        return DATE_FORMAT.parse(date).getTime();
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

}
