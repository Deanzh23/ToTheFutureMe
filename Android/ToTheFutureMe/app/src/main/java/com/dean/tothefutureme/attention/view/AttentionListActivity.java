package com.dean.tothefutureme.attention.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.attention.model.AttentionModel;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityAttentionListBinding;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.utils.TokenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 好友Activity
 * <p>
 * Created by dean on 2018/1/5.
 */
@ContentView(R.layout.activity_attention_list)
public class AttentionListActivity extends ConvenientActivity<ActivityAttentionListBinding> implements Toolbar.OnMenuItemClickListener {

    private AttentionAdapter attentionAdapter;

    private List<AttentionModel> attentionModels;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // 数据更新，重新获取数据
            if (AppConfig.BROADCAST_RECEIVER_ATTENTION_UPDATE.equals(action))
                loadData();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle("关注列表");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> AttentionListActivity.this.finish());
        viewDataBinding.toolbar.setOnMenuItemClickListener(this);

        loadData();

        IntentFilter intentFilter = new IntentFilter(AppConfig.BROADCAST_RECEIVER_ATTENTION_UPDATE);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // 跳转 添加关注界面
        startActivity(new Intent(this, AddAttentionActivity.class));
        return true;
    }

    /**
     * 获取好友列表数据
     */
    private void loadData() {
        viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.messageLayout);

        new Thread(() -> {
            List<String> urlParams = new ArrayList<>();
            urlParams.add(TTFMApplication.getAuthModel().getToken());

            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("username", TTFMApplication.getAuthModel().getUsername());

            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.ATTENTION_LOAD_FRIEND, null, urlParams, bodyParams,
                    new OnHttpConnectionListener() {
                        @Override
                        public void onSuccess(String s) {
                            AttentionListActivity.this.runOnUiThread(() -> {
                                try {
                                    JSONObject response = new JSONObject(s);
                                    String code = response.getString("code");
                                    if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                        JSONArray array = response.getJSONArray("data");
                                        attentionModels = JSONUtil.jsonArray2List(array, AttentionModel.class);

                                        setFriendData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(-100);
                                }
                            });
                        }

                        @Override
                        public void onError(int i) {
                            AttentionListActivity.this.runOnUiThread(() -> ToastUtil.showToast(AttentionListActivity.this, "获取关注列表失败 " + i));
                        }

                        @Override
                        public void onTokenFailure() {
                            AttentionListActivity.this.runOnUiThread(() -> TokenUtils.loginAgain(AttentionListActivity.this));
                        }

                        @Override
                        public void onEnd() {
                            AttentionListActivity.this.runOnUiThread(() -> viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.messageLayout));
                        }
                    });
        }).start();
    }

    /**
     * 设置列表数据
     */
    private void setFriendData() {
        if (attentionAdapter == null) {
            attentionAdapter = new AttentionAdapter(AttentionListActivity.this, attentionModels);
            viewDataBinding.friendListView.setAdapter(attentionAdapter);
        } else
            attentionAdapter.update(attentionModels);

        viewDataBinding.friendListView.setVisibility(SetUtil.isEmpty(attentionModels) ? View.GONE : View.VISIBLE);
        viewDataBinding.messageLineTextView.setVisibility(SetUtil.isEmpty(attentionModels) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);

        super.onDestroy();
    }
}
