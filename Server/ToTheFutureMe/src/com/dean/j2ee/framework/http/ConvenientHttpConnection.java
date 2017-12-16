package com.dean.j2ee.framework.http;

import com.dean.j2ee.framework.http.listener.HttpConnectionListener;

import java.util.Map;

/**
 * Convenient http connection
 * <p>
 * Created by Dean on 16/8/8.
 */
public class ConvenientHttpConnection extends DefaultHttpConnection {

    /**
     * 发送（默认配置的） Http Get 请求
     *
     * @param basicURL
     * @param params
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> headerParams, Object params, HttpConnectionListener httpConnectionListener) {
        super.sendHttpGet(basicURL, headerParams, params, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送（默认配置的） Http Post 请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, Map<String, String> bodyParams,
                             HttpConnectionListener httpConnectionListener) {
        super.sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送（默认配置的） Http Post 请求
     * <p>
     * body参数是复杂的形式
     *
     * @param basicURL
     * @param headerParams
     * @param params
     * @param bodyParams
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, String bodyParams, HttpConnectionListener httpConnectionListener) {
        super.sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, httpConnectionListener);
    }

}
