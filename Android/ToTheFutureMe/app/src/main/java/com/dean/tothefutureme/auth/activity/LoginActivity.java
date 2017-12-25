package com.dean.tothefutureme.auth.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.auth.model.AuthModel;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityLoginBinding;
import com.dean.tothefutureme.home.HomeActivity;
import com.dean.tothefutureme.main.TTFMApplication;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 登陆Activity
 * <p>
 * Created by dean on 2017/12/3.
 */
@Permission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
@ContentView(R.layout.activity_login)
public class LoginActivity extends ConvenientActivity<ActivityLoginBinding> {

    private ProgressDialog progressDialog;

    private Handler handler = new Handler();

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
        if (TextUtils.isEmpty(TTFMApplication.getAuthModel().getUsername())) {
            ToastUtil.showToast(this, "请填写邮箱用户名");
            return;
        }

        if (TextUtils.isEmpty(TTFMApplication.getAuthModel().getPassword())) {
            ToastUtil.showToast(this, "请填写密码");
            return;
        }

        progressDialog = ConvenientProgressDialog.getInstance(LoginActivity.this, "正在登陆...", false);
        progressDialog.show();

        new Thread(() -> {
            List<Object> urlParams = new ArrayList<>();
            urlParams.add(TTFMApplication.getAuthModel().getUsername());
            urlParams.add(TTFMApplication.getAuthModel().getPassword());

            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.AUTH_LOGIN, null, urlParams, (Map<String, String>) null,
                    new HttpConnectionListener() {
                        @Override
                        public void onSuccess(String s) {
                            // 跳转到Home page
                            handler.post(() -> {
                                try {
                                    JSONObject response = new JSONObject(s);
                                    String code = response.getString("code");
                                    if ("401".equals(code)) {
                                        ToastUtil.showToast(LoginActivity.this, "账号不存在");
                                        return;
                                    } else if ("400".equals(code)) {
                                        ToastUtil.showToast(LoginActivity.this, "账号或密码错误");
                                        return;
                                    } else {
                                        // 保存登陆用户信息到数据
                                        new Thread(() -> {
                                            try {
                                                JSONObject authJSONObject = response.getJSONObject("data");
                                                DatabaseUtil.deleteAll(AuthModel.class, null);
                                                TTFMApplication.setAuthModel(JSONUtil.json2Object(authJSONObject, AuthModel.class));
                                                DatabaseUtil.saveOrUpdate(TTFMApplication.getAuthModel());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                onError(-1);
                                            }
                                        }).start();

                                        MobclickAgent.onProfileSignIn(TTFMApplication.getAuthModel().getUsername());
                                        // 进入APP
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        ConvenientApplication.killHistoryActivity(HomeActivity.class.getSimpleName());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(-2);
                                }
                            });
                        }

                        @Override
                        public void onError(int i) {
                            handler.post(() -> ToastUtil.showToast(LoginActivity.this, "登陆请求失败 " + i));
                        }

                        @Override
                        public void onTokenFailure() {
                        }

                        @Override
                        public void onEnd() {
                            handler.post(() -> progressDialog.dismiss());
                        }
                    });
        }).start();

    }

    /**
     * 跳转注册
     */
    @OnClick(R.id.registerTextView)
    public void toRegister() {
        startActivity(new Intent(LoginActivity.this, CheckUsernameActivity.class));
    }
}
