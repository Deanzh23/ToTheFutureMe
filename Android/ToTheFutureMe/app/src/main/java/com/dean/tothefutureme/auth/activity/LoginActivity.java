package com.dean.tothefutureme.auth.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityLoginBinding;
import com.dean.tothefutureme.home.HomeActivity;
import com.dean.tothefutureme.main.TTFMApplication;

/**
 * 登陆Activity
 * <p>
 * Created by dean on 2017/12/3.
 */
@Permission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
@ContentView(R.layout.activity_login)
public class LoginActivity extends ConvenientActivity<ActivityLoginBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    @OnClick(R.id.loginButton)
    public void login() {
        /**
         * debug
         */
        startActivity(new Intent(this, HomeActivity.class));
        ConvenientApplication.killHistoryActivity(HomeActivity.class.getSimpleName());
    }

    /**
     * 跳转注册
     */
    @OnClick(R.id.registerTextView)
    public void toRegister() {
        startActivity(new Intent(LoginActivity.this, CheckUsernameActivity.class));
    }
}
