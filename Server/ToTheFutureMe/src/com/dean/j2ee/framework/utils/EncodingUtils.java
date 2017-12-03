package com.dean.j2ee.framework.utils;

import java.io.UnsupportedEncodingException;

/**
 * 转码工具类
 * <p>
 * Created by dean on 2017/2/8.
 */
public class EncodingUtils {

    /**
     * ISO转UTF-8
     *
     * @param str
     * @return
     */
    public static String encodeISO2UTF(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
