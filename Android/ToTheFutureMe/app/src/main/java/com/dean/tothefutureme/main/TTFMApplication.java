package com.dean.tothefutureme.main;

import android.util.Log;

import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.util.CodeUtils;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.tothefutureme.auth.model.AuthModel;
import com.dean.tothefutureme.config.AppConfig;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

import io.yunba.android.manager.YunBaManager;

/**
 * 给未来的自己Application
 * <p>
 * Created by dean on 2017/12/3.
 */
public class TTFMApplication extends ConvenientApplication {

    private static AuthModel authModel;

    private static TTFMApplication instance;

    public static TTFMApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        if (authModel == null)
            authModel = new AuthModel();
    }

    @Override
    protected void initConfigAndData() {
        // 初始化数据库
        DatabaseUtil.init(this, "ttfm_db", 1, null);
        // 查找之前的登陆用户
        List<AuthModel> authModels = DatabaseUtil.findAll(AuthModel.class, null);
        authModel = SetUtil.isEmpty(authModels) ? getAuthModel() : authModels.get(0);

        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String setVersionUpdateDownloadLocalPath() {
        return null;
    }

    @Override
    protected String checkVersionUrl() {
        return null;
    }

    /**
     * 启动个推推送
     */
    public static void startYunBaPush() {
        // 启动推送服务
        YunBaManager.start(instance.getApplicationContext());
        YunBaManager.resume(instance.getApplicationContext());
        YunBaManager.subscribe(instance.getApplicationContext(), new String[]{CodeUtils.md5Encode(TTFMApplication.getAuthModel().getUsername())},
                new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken arg0) {
                        Log.d(AppConfig.TAG_YUN_BA, "Subscribe topic succeed");
                    }

                    @Override
                    public void onFailure(IMqttToken arg0, Throwable arg1) {
                        Log.d(AppConfig.TAG_YUN_BA, "Subscribe topic failed");
                    }
                });
        // 监听用户上/下线
        YunBaManager.subscribePresence(instance.getApplicationContext(), CodeUtils.md5Encode(TTFMApplication.getAuthModel().getUsername()), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken mqttToken) {
                        Log.d(AppConfig.TAG_YUN_BA, "subscribePresence to topic succeed");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        if (exception instanceof MqttException) {
                            MqttException ex = (MqttException) exception;

                            Log.d(AppConfig.TAG_YUN_BA, "subscribePresence failed with error code : " + ex.getReasonCode());
                        }
                    }
                }
        );
    }

    public static AuthModel getAuthModel() {
        if (authModel == null)
            authModel = new AuthModel();

        return authModel;
    }

    public static void setAuthModel(AuthModel authModel) {
        TTFMApplication.authModel = authModel;
    }
}
