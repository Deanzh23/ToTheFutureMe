package com.dean.j2ee.framework.http;

import java.util.Map;

/**
 * Created by Dean on 2016/12/1.
 */
public class HttpConnection extends DefaultHttpConnection {

    /**
     * 发送（默认配置的） Http Get 请求
     *
     * @param basicURL
     * @param params
     */
    public void sendHttpGet(String basicURL, Map<String, String> params) {
        sendHttpGet(basicURL, params, "utf-8", 5000, false);
    }

    /**
     * 发送（默认配置的） Http Post 请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     */
    public String sendHttpPost(String basicURL, Map<String, String> params, String bodyParams) {
        return sendHttpPost(basicURL, params, bodyParams, "utf-8", 5000, false);
    }


}
