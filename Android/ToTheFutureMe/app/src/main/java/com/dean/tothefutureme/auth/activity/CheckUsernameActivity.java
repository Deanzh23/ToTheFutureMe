package com.dean.tothefutureme.auth.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityCheckUsernameBinding;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.utils.TokenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户名可用性检测Activity
 * <p>
 * Created by dean on 2017/12/3.
 */
@ContentView(R.layout.activity_check_username)
public class CheckUsernameActivity extends ConvenientActivity<ActivityCheckUsernameBinding> {

    private ProgressDialog progressDialog;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle("检查账号");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> CheckUsernameActivity.this.finish());

        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    /**
     * 开始请求检测邮箱可用性
     */
    @OnClick(R.id.checkUsernameTextView)
    public void checkUsername() {
        String username = TTFMApplication.getAuthModel().getUsername();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast(this, "请填写邮箱");
            return;
        }

        progressDialog = ConvenientProgressDialog.getInstance(CheckUsernameActivity.this, "正在检测，请稍后...", false);
        progressDialog.show();

        new Thread(() -> {
            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            Map<String, String> bodyParams = new LinkedHashMap<>();
            bodyParams.put("username", username);
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.AUTH_CHECK_USERNAME, null, null, bodyParams,
                    new OnHttpConnectionListener() {
                        @Override
                        public void onSuccess(String s) {
                            handler.post(() -> {
                                try {
                                    JSONObject response = new JSONObject(s);
                                    String code = response.getString("code");

                                    if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                        ToastUtil.showToast(CheckUsernameActivity.this, "验证码邮件已发送您的邮箱", Toast.LENGTH_LONG);
                                        startActivity(new Intent(CheckUsernameActivity.this, RegisterActivity.class));
                                        CheckUsernameActivity.this.finish();
                                    } else if ("400".equals(code))
                                        ToastUtil.showToast(CheckUsernameActivity.this, "邮箱用户名已被占用，请更换邮箱用户名", Toast.LENGTH_LONG);
                                    else
                                        onError(-2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(-1);
                                }
                            });
                        }

                        @Override
                        public void onError(int i) {
                            handler.post(() -> ToastUtil.showToast(CheckUsernameActivity.this, "检测账号可用性失败"));
                        }

                        @Override
                        public void onTokenFailure() {
                            handler.post(() -> TokenUtils.loginAgain(CheckUsernameActivity.this));
                        }

                        @Override
                        public void onEnd() {
                            handler.post(() -> progressDialog.dismiss());
                        }
                    });
        }).start();
    }

}
