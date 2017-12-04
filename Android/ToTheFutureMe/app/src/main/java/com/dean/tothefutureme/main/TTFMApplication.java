package com.dean.tothefutureme.main;

import android.os.Environment;

import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.tothefutureme.auth.model.AuthModel;

/**
 * 给未来的自己Application
 * <p>
 * Created by dean on 2017/12/3.
 */
public class TTFMApplication extends ConvenientApplication {

    public static final String BASE_URL = "";
    /**
     * app基础SD卡路径
     */
    public static final String APP_BASE_PATH = Environment.getExternalStorageDirectory().getPath() + "/ttfm/";
    public static final String APP_IMAGE_PAT = APP_BASE_PATH + "image/";

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
        DatabaseUtil.init(this, "ttfm_db", 1, null);

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

    public static AuthModel getAuthModel() {
        if (authModel == null)
            authModel = new AuthModel();

        return authModel;
    }

    public static void setAuthModel(AuthModel authModel) {
        TTFMApplication.authModel = authModel;
    }
}
