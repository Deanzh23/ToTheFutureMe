package com.dean.tothefutureme.config;

import android.os.Environment;

/**
 * App配置
 * <p>
 * Created by dean on 2017/12/7.
 */
public class AppConfig {

    /**
     * 正式环境域名
     */
    public static final String BASE_URL = "http://deanzh.7766.org:8444/ttfm/";
    /**
     * 本地开发环境域名
     */
//    public static final String BASE_URL = "http://192.168.0.105:8444/ttfm/";
    /**
     * 公司本地开发环境域名
     */
//    public static final String BASE_URL = "http://10.88.54.198:8444/ttfm/";
    /**
     * 验证账号可用性（邮箱）
     */
    public static final String AUTH_CHECK_USERNAME = "auth/check";
    /**
     * 重新发送验证码
     */
    public static final String AUTH_SEND_VERIFICATION_CODE_AGAIN = "verification";
    /**
     * 用户注册
     */
    public static final String AUTH_REGISTER = "auth/register";
    /**
     * 用户登陆
     */
    public static final String AUTH_LOGIN = "auth/login";
    /**
     * 更新用户信息
     */
    public static final String AUTH_UPLOAD = "auth/upload";
    /**
     * 修改密码
     */
    public static final String AUTH_EDIT_PASSWORD = "auth/editPassword";
    /**
     * 上传信件
     */
    public static final String LETTER_UPLOAD = "letter/upload";
    /**
     * 读取信件
     */
    public static final String LETTER_LOAD = "letter/load";
    /**
     * 设置信件已读
     */
    public static final String LETTER_LOAD_READ = "letter/readLetter";
    /**
     * 图片上传
     */
    public static final String FILE = "file/upload";
    /**
     * 应答"成功"
     */
    public static final String RESPONSE_SUCCESS = "200";

    /**
     * app基础SD卡路径
     */
    public static final String APP_BASE_PATH = Environment.getExternalStorageDirectory().getPath() + "/TTFM";
    public static final String APP_IMAGE_PAT = APP_BASE_PATH + "image/";

    /**
     * 图片类型
     */
    // 头像
    public static final int IMAGE_TYPE_AVATAR = 0;

    /**
     * 数据更新广播标识
     */
    public static final String BROADCAST_RECEIVER_DATA_UPDATE = "broadcast_receiver_data_update";
    /**
     * 信件已读更新广播标识
     */
    public static final String BROADCAST_RECEIVER_LETTER_READ_UPDATE = "broadcast_receiver_letter_read_update";

    public static final String TAG_YUN_BA = "YunBa SDK";
    /**
     * 通知类型
     */
    public static final int NOTIFICATION_TYPE = 1;

}
