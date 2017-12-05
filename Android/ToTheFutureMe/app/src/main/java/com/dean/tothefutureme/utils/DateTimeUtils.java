package com.dean.tothefutureme.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期时间工具类
 * <p>
 * Created by dean on 2017/12/5.
 */
public class DateTimeUtils {

    public static final SimpleDateFormat DATE_MILLI_SECOND_FORMAT = new SimpleDateFormat("yyyy/MM/DD");

    /**
     * 获取指定日期的毫秒数形式值
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static long getDateMillisecond(String date) throws ParseException {
        return DATE_MILLI_SECOND_FORMAT.parse(date).getTime();
    }

}
