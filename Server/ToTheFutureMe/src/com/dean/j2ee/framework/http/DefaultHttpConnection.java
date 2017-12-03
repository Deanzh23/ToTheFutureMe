package com.dean.j2ee.framework.http;

import com.dean.j2ee.framework.concentration.ConcentrationUtil;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * HTTP请求类
 * <p>
 * Created by Dean on 2016/12/1.
 */
public class DefaultHttpConnection {

    // 应答"成功"
    public static final String RESPONSE_SUCCESS = "200";
    // 应答"失败"
    public static final String RESPONSE_FAILURE = "400";

    /**
     * 发送HttpGet请求
     *
     * @param basicURL
     * @param params
     * @param encoding
     * @param timeOut
     * @param isUseCache
     */
    public String sendHttpGet(String basicURL, Map<String, String> params, String encoding, int timeOut, boolean isUseCache) {
        StringBuilder builder = new StringBuilder(basicURL);

        int i = 0;
        if (!ConcentrationUtil.isEmpty(params))
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                builder.append(i++ == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode(value));
            }

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(builder.toString());

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "GET", encoding, timeOut, isUseCache);

            connection.connect();

            /** 响应 **/
            int responseCode = connection.getResponseCode();

            // 请求成功
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                StringBuilder resopnseBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                    resopnseBuilder.append(line);

                return resopnseBuilder.length() <= 0 ? null : resopnseBuilder.toString();
            }
            // 请求失败
            else {
                return RESPONSE_FAILURE;
            }
        } catch (IOException e) {
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return RESPONSE_FAILURE;
    }

    /**
     * 发送HttpPost请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param encoding
     * @param timeOut
     * @param isUseCache
     */
    public String sendHttpPost(String basicURL, Map<String, String> params, String bodyParams, String encoding, int timeOut, boolean isUseCache) {
        StringBuilder builder = new StringBuilder(basicURL);

        int i = 0;
        if (!ConcentrationUtil.isEmpty(params))

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                builder.append(i == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode(value));

                i++;
            }

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(builder.toString());

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "POST", encoding, timeOut, isUseCache);

            connection.connect();

            /** body **/
            if (bodyParams != null) {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(bodyParams.getBytes());
                outputStream.flush();
                outputStream.close();
            }

            /** 响应 **/
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    responseBuilder.append(line);

                return responseBuilder.length() == 0 ? RESPONSE_SUCCESS : responseBuilder.toString();

            } else
                return RESPONSE_FAILURE;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
            }
        }

        return RESPONSE_FAILURE;
    }

    /**
     * 设置连接规则
     *
     * @param connection
     * @param encoding
     * @param timeOut
     * @param isUseCache
     */
    private void setHttpURLConnectionConfig(HttpURLConnection connection, String requestMethod, String encoding, int timeOut, boolean isUseCache) throws ProtocolException {
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("encoding", encoding);
        connection.setConnectTimeout(timeOut);
        connection.setUseCaches(isUseCache);

        boolean isGetMethod = (requestMethod != null && requestMethod.toLowerCase().equals("get"));
        connection.setDoOutput(!isGetMethod);
        connection.setDoInput(true);
    }

}
