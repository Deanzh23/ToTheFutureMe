package com.dean.tothefutureme.config;

import android.os.Environment;

/**
 * App配置
 * <p>
 * Created by dean on 2017/12/7.
 */
public class AppConfig {

    /**
     * 域名
     */
    public static final String BASE_URL = "http://192.168.0.105:8444/ttfm/";
    /**
     * 验证账号可用性（邮箱）
     */
    public static final String AUTH_CHECK_USERNAME = "auth/check";
    /**
     * 用户登陆
     */
    public static final String AUTH_LOGIN = "auth/login";

    /**
     * app基础SD卡路径
     */
    public static final String APP_BASE_PATH = Environment.getExternalStorageDirectory().getPath() + "/ttfm/";
    public static final String APP_IMAGE_PAT = APP_BASE_PATH + "image/";

}
