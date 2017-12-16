package com.dean.j2ee.framework.http;


import com.dean.j2ee.framework.http.listener.HttpConnectionListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

/**
 * Http请求超类
 * <p>
 * Created by Dean on 16/8/9.
 */
public class DefaultHttpConnection {

    /**
     * 发送默认配置的HttpGet请求
     *
     * @param basicURL
     * @param params
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> headerParams, Object params, HttpConnectionListener httpConnectionListener) {
        sendHttpGet(basicURL, headerParams, params, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送HttpGet请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param encoding
     * @param timeOut
     * @param isUseCache
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> headerParams, Object urlParams, String encoding, int timeOut, boolean isUseCache,
                            HttpConnectionListener httpConnectionListener) {
        String urlParam = getHttpURL(basicURL, urlParams);

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlParam);

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "GET", encoding, timeOut, isUseCache, headerParams);

            connection.connect();

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

                if (httpConnectionListener != null)
                    httpConnectionListener.success(responseBuilder.length() == 0 ? null : responseBuilder.toString());
            } else {
                if (httpConnectionListener != null)
                    httpConnectionListener.error(responseCode);
            }
        } catch (MalformedURLException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } catch (IOException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } finally {
            if (httpConnectionListener != null)
                httpConnectionListener.end();

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
    }

    /**
     * 发送默认配置的HttpPost请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, Map<String, String> bodyParams,
                             HttpConnectionListener httpConnectionListener) {
        sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送HttpPost请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param bodyParams
     * @param encoding
     * @param timeOut
     * @param isUseCache
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object urlParams, Object bodyParams, String encoding, int timeOut, boolean isUseCache,
                             HttpConnectionListener httpConnectionListener) {
        String urlParam = getHttpURL(basicURL, urlParams);

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlParam);

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "POST", encoding, timeOut, isUseCache, headerParams);

            connection.connect();

            /** body **/
            JSONObject bodyJSONObject = null;
            String bodyString = null;

            if (bodyParams != null) {
                if (bodyParams instanceof Map) {
                    Map<String, String> bodyMap = (Map<String, String>) bodyParams;

                    if (bodyMap == null || bodyMap.size() <= 0)
                        return;

                    bodyJSONObject = new JSONObject();

                    for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        bodyJSONObject.put(URLEncoder.encode(key, "utf-8"), URLEncoder.encode(value, "utf-8"));
                    }
                } else if (bodyParams instanceof String) {
                    bodyString = (String) bodyParams;
                }
            }

            OutputStream outputStream = connection.getOutputStream();
            if (bodyJSONObject != null)
                outputStream.write(bodyJSONObject.toString().getBytes());
            else if (bodyString != null)
                outputStream.write(bodyString.getBytes());
            outputStream.flush();
            outputStream.close();

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

                if (httpConnectionListener != null)
                    httpConnectionListener.success(responseBuilder.length() == 0 ? null : responseBuilder.toString());
            } else {
                if (httpConnectionListener != null)
                    httpConnectionListener.error(responseCode);
            }
        } catch (MalformedURLException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } catch (IOException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } catch (JSONException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } finally {
            if (httpConnectionListener != null)
                httpConnectionListener.end();

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
    }

    /**
     * 设置连接规则
     *
     * @param connection
     * @param encoding
     * @param timeOut
     * @param isUseCache
     */
    private void setHttpURLConnectionConfig(HttpURLConnection connection, String requestMethod, String encoding, int timeOut, boolean isUseCache,
                                            Map<String, String> headerParams) throws ProtocolException {
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("encoding", encoding);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setConnectTimeout(timeOut);
        connection.setUseCaches(isUseCache);

        // 设置用户header参数
        if (headerParams != null && headerParams.size() > 0) {
            for (Map.Entry<String, String> entry : headerParams.entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        boolean isGetMethod = (requestMethod != null && requestMethod.toLowerCase().equals("get"));
        connection.setDoOutput(!isGetMethod);
        connection.setDoInput(true);
    }

    /**
     * 获取配置好的URL
     *
     * @param basicURL
     * @param urlParams
     * @return
     */
    private String getHttpURL(String basicURL, Object urlParams) {
        StringBuilder builder = new StringBuilder(basicURL);

        if (urlParams instanceof Map) {
            Map<String, Object> tempURLParams = (Map<String, Object>) urlParams;

            int i = 0;
            if (tempURLParams != null && tempURLParams.size() > 0)

                for (Map.Entry<String, Object> entry : tempURLParams.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof String) {
                        builder.append(i == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode((String) value));
                    } else {
                        builder.append(i == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(value);
                    }

                    i++;
                }
        } else if (urlParams instanceof List) {
            for (Object value : (List<Object>) urlParams) {
                if (value instanceof String) {
                    builder.append("/").append(URLEncoder.encode((String) value));
                } else {
                    builder.append("/").append(value);
                }
            }
        }

        return builder.toString();
    }

}
