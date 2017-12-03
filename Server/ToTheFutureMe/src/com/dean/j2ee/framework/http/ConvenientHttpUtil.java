package com.dean.j2ee.framework.http;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 架构请求工具类
 * <p>
 * Created by Dean on 2016/12/1.
 */
public class ConvenientHttpUtil {

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;

    /**
     * 发送http请求
     *
     * @param method
     * @param url
     * @param urlParams
     * @param bodyParam
     * @return
     */
    public static String sendHttp(int method, String url, Map<String, String> urlParams, JSONObject bodyParam) throws UnsupportedEncodingException {
        String response = null;

        switch (method) {
            case METHOD_GET:
                response = sendHttpGet(url, urlParams);
                break;
            case METHOD_POST:
                response = sendHttpPost(url, urlParams, bodyParam);
                break;
        }

        return response;
    }

    /**
     * 发送http get请求
     *
     * @param url
     * @param urlParams
     * @return
     */
    private static String sendHttpGet(String url, Map<String, String> urlParams) {
        String response = null;

        return response;
    }

    /**
     * 发送http post请求
     *
     * @param url
     * @param urlParams
     * @param bodyParam
     * @return
     */
    private static String sendHttpPost(String url, Map<String, String> urlParams, JSONObject bodyParam) throws UnsupportedEncodingException {
        String response = null;

        return response;
    }

}
