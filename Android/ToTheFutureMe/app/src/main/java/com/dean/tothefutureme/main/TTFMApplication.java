package com.dean.tothefutureme.main;

import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.tothefutureme.auth.model.AuthModel;

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
    }

    @Override
    protected void initConfigAndData() {
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
        return authModel;
    }

    public static void setAuthModel(AuthModel authModel) {
        TTFMApplication.authModel = authModel;
    }
}
