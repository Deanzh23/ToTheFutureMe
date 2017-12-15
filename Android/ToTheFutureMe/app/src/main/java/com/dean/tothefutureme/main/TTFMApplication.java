package com.dean.tothefutureme.main;

import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.tothefutureme.auth.model.AuthModel;
import com.dean.tothefutureme.push.TTFMIntentService;
import com.dean.tothefutureme.push.TTFMPushService;
import com.igexin.sdk.PushManager;

import java.util.List;

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
    public static void startGeTuiPush() {
        PushManager.getInstance().initialize(instance.getApplicationContext(), TTFMPushService.class);
        PushManager.getInstance().registerPushIntentService(instance.getApplicationContext(), TTFMIntentService.class);
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
