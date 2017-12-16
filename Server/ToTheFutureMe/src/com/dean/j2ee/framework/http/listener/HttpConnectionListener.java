package com.dean.j2ee.framework.http.listener;

/**
 * 网络请求状态监听器
 * <p>
 * Created by Dean on 16/8/9.
 */
public interface HttpConnectionListener {

    /**
     * 请求成功（返回状态码200）
     *
     * @param response
     */
    void success(String response);

    /**
     * 请求失败（返回状态码非200的其它）
     *
     * @param responseCode
     */
    void error(int responseCode);

    /**
     * 请求完成
     */
    void end();

}
