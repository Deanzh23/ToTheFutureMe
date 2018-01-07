package com.dean.tothefutureme.attention.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.attention.model.AttentionModel;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityAddAttentionBinding;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.utils.TokenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加关注 Activity
 * <p>
 * Created by dean on 2018/1/5.
 */
@ContentView(R.layout.activity_add_attention)
public class AddAttentionActivity extends ConvenientActivity<ActivityAddAttentionBinding> implements TextView.OnEditorActionListener, TextWatcher {

    private AttentionModel attentionModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle("添加关注");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> AddAttentionActivity.this.finish());
        viewDataBinding.searchEditText.addTextChangedListener(this);
        // 设置软键盘搜索监听
        viewDataBinding.searchEditText.setOnEditorActionListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.contentLayout);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // 点击的是软键盘的搜索键
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            KeyboardUtil.hideSoftKeyboard(AddAttentionActivity.this);

            if (TextUtils.isEmpty(viewDataBinding.searchEditText.getText())) {
                ToastUtil.showToast(AddAttentionActivity.this, "亲，输入后再搜索哟");
            } else {
                viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.contentLayout);
                search();
            }

            return true;
        }

        return false;
    }

    /**
     * 搜索用户
     */
    private void search() {
        new Thread(() -> {
            List<String> urlParams = new ArrayList<>();
            urlParams.add(TTFMApplication.getAuthModel().getToken());

            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("username", viewDataBinding.searchEditText.getText().toString());

            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.ATTENTION_SEARCH_AUTH_BY_USERNAME, null, urlParams, bodyParams,
                    new OnHttpConnectionListener() {
                        @Override
                        public void onSuccess(String s) {
                            AddAttentionActivity.this.runOnUiThread(() -> {
                                try {
                                    JSONObject response = new JSONObject(s);
                                    String code = response.getString("code");
                                    boolean hasData = response.getBoolean("hasData");

                                    if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                        if (hasData)
                                            attentionModel = JSONUtil.json2Object(response.getJSONObject("data"), AttentionModel.class);

                                        setData(hasData);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(-100);
                                }
                            });
                        }

                        @Override
                        public void onError(int i) {
                            AddAttentionActivity.this.runOnUiThread(() -> ToastUtil.showToast(AddAttentionActivity.this, "搜索失败 " + i));
                        }

                        @Override
                        public void onTokenFailure() {
                            AddAttentionActivity.this.runOnUiThread(() -> TokenUtils.loginAgain(AddAttentionActivity.this));
                        }

                        @Override
                        public void onEnd() {
//                            AddAttentionActivity.this.runOnUiThread(() -> viewDataBinding.elasticityLoadingView.stop());
                        }
                    });
        }).start();
    }

    private Handler handler = new Handler();

    /**
     * 设置显示数据
     *
     * @param hasData
     */
    private void setData(boolean hasData) {
        handler.postDelayed(() -> {
            viewDataBinding.noDataMessageTextView.setVisibility(hasData ? View.GONE : View.VISIBLE);
            viewDataBinding.attentionLayout.setVisibility(hasData ? View.VISIBLE : View.GONE);

            viewDataBinding.noDataMessageTextView.setText(hasData ? "" : "没有此账号的用户");
            if (hasData && attentionModel != null) {
                BitmapUtil.imageLoader(viewDataBinding.avatarImageView, AppConfig.BASE_URL + attentionModel.getAvatarUrl(), AppConfig.APP_IMAGE_PAT,
                        false);
                viewDataBinding.nicknameTextView.setText(attentionModel.getNickname());
                viewDataBinding.usernameTextView.setText(attentionModel.getUsername());
            }

            viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.contentLayout);
        }, 3000);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        viewDataBinding.noDataMessageTextView.setText("");
    }
}
