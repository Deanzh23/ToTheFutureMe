package com.dean.tothefutureme.auth.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityCheckUsernameBinding;
import com.dean.tothefutureme.main.TTFMApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户名可用性检测Activity
 * <p>
 * Created by dean on 2017/12/3.
 */
@ContentView(R.layout.activity_check_username)
public class CheckUsernameActivity extends ConvenientActivity<ActivityCheckUsernameBinding> {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());
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

        ConvenientHttpConnection connection = new ConvenientHttpConnection();
        List<Object> urlParams = new ArrayList<>();
        urlParams.add(username);
        connection.sendHttpPost(TTFMApplication.BASE_URL + "", null, urlParams, (Map<String, String>) null, new HttpConnectionListener() {
            @Override
            public void success(String s) {
                ToastUtil.showToast(CheckUsernameActivity.this, "邮箱可以使用");
            }

            @Override
            public void error(int i) {
                ToastUtil.showToast(CheckUsernameActivity.this, "邮箱已被使用");

                /**
                 * debug code
                 */
                startActivity(new Intent(CheckUsernameActivity.this, RegisterActivity.class));
                CheckUsernameActivity.this.finish();
            }

            @Override
            public void end() {
                progressDialog.dismiss();
            }
        });
    }

}
