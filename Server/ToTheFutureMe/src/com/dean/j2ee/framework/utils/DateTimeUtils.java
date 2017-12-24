package com.dean.j2ee.framework.utils;

import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateTimeUtils {

    /**
     * 计算相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

}
