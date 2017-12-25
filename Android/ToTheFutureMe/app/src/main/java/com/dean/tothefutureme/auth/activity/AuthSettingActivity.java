package com.dean.tothefutureme.auth.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.CodeUtils;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityAuthSettingBinding;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.utils.TokenUtils;
import com.umeng.analytics.MobclickAgent;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.yunba.android.manager.YunBaManager;

/**
 * 账号设置 Activity
 * <p>
 * Created by dean on 2017/12/13.
 */
@ContentView(R.layout.activity_auth_setting)
public class AuthSettingActivity extends ConvenientActivity<ActivityAuthSettingBinding> {

    private ProgressDialog waitDialog;
    private AlertDialog editPasswordDialog;
    private AlertDialog exitLoginDialog;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle("账号设置");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> AuthSettingActivity.this.finish());
    }

    /**
     * 提交修改密码
     */
    @OnClick(R.id.uploadButton)
    public void uploadPassword() {
        // 条件检查
        if (TextUtils.isEmpty(viewDataBinding.oldPasswordEditText.getText()) || TextUtils.isEmpty(viewDataBinding.passwordEditText.getText()) ||
                TextUtils.isEmpty(viewDataBinding.confirmPasswordEditText.getText())) {
            ToastUtil.showToast(this, "信息填写不完整");
            return;
        } else if (!TTFMApplication.getAuthModel().getPassword().equals(viewDataBinding.oldPasswordEditText.getText())) {
            ToastUtil.showToast(this, "旧密码不正确");
            return;
        } else if (!viewDataBinding.passwordEditText.getText().equals(viewDataBinding.confirmPasswordEditText.getText())) {
            ToastUtil.showToast(this, "两次输入的密码不相同");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定提交密码修改吗？");
        builder.setNeutralButton("确定", (dialog, which) -> {
            if (editPasswordDialog != null)
                editPasswordDialog.dismiss();
            // 提交密码修改
            commitEditPassword();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            if (editPasswordDialog != null)
                editPasswordDialog.dismiss();
        });
        builder.setCancelable(true);
        editPasswordDialog = builder.create();
        editPasswordDialog.show();
    }

    /**
     * 退出登陆
     */
    @OnClick(R.id.exitLoginButton)
    public void exitLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定退出登陆账号吗？");
        builder.setNegativeButton("退出登陆", (dialog, which) -> {
            if (exitLoginDialog != null)
                exitLoginDialog.dismiss();

            // 退出推送接收
            YunBaManager.unsubscribe(TTFMApplication.getInstance().getApplicationContext(), CodeUtils.md5Encode(TTFMApplication.getAuthModel().getUsername()),
                    new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken iMqttToken) {
                            Log.d(AppConfig.TAG_YUN_BA, "停止推送接收成功");
                        }

                        @Override
                        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                            Log.e(AppConfig.TAG_YUN_BA, "停止推送接收失败！");
                        }
                    });

            new Thread(() -> {
                TTFMApplication.getAuthModel().setPassword("");
                TTFMApplication.getAuthModel().setToken(null);
                DatabaseUtil.saveOrUpdate(TTFMApplication.getAuthModel());

                handler.post(() -> {
                    MobclickAgent.onProfileSignOff();
                    AuthSettingActivity.this.startActivity(new Intent(AuthSettingActivity.this, LoginActivity.class));
                    TTFMApplication.killHistoryActivity(LoginActivity.class.getSimpleName());
                });
            }).start();
        });
        builder.setPositiveButton("取消", (dialog, which) -> {
            if (exitLoginDialog != null)
                exitLoginDialog.dismiss();
        });
        builder.setCancelable(true);
        exitLoginDialog = builder.create();
        exitLoginDialog.show();
    }

    /**
     * 提交密码修改
     */
    private void commitEditPassword() {
        waitDialog = ConvenientProgressDialog.getInstance(this, "正在修改...", false);
        waitDialog.show();

        new Thread(() -> {
            try {
                List<String> urlParams = new ArrayList<>();
                urlParams.add(TTFMApplication.getAuthModel().getToken());

                JSONObject request = new JSONObject();
                request.put("username", TTFMApplication.getAuthModel().getUsername());
                request.put("oldPassword", viewDataBinding.oldPasswordEditText.getText().toString());
                request.put("newPassword", viewDataBinding.passwordEditText.getText().toString());

                ConvenientHttpConnection connection = new ConvenientHttpConnection();
                connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.AUTH_EDIT_PASSWORD, null, urlParams, request.toString(),
                        new HttpConnectionListener() {
                            @Override
                            public void onSuccess(String s) {
                                try {
                                    JSONObject response = new JSONObject(s);
                                    String code = response.getString("code");

                                    if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                        String password = response.getString("data");
                                        TTFMApplication.getAuthModel().setPassword(password);
                                        // 更新账号信息到数据库
                                        DatabaseUtil.saveOrUpdate(TTFMApplication.getAuthModel());

                                        handler.post(() -> {
                                            ToastUtil.showToast(AuthSettingActivity.this, "密码修改成功");
                                            AuthSettingActivity.this.finish();
                                        });
                                    } else
                                        onError(-2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(-1);
                                }
                            }

                            @Override
                            public void onError(int i) {
                                handler.post(() -> ToastUtil.showToast(AuthSettingActivity.this, "密码修改失败 " + i));
                            }

                            @Override
                            public void onTokenFailure() {
                                handler.post(() -> TokenUtils.loginAgain(AuthSettingActivity.this));
                            }

                            @Override
                            public void onEnd() {
                                handler.post(() -> {
                                    if (waitDialog != null && waitDialog.isShowing())
                                        waitDialog.dismiss();
                                });
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
