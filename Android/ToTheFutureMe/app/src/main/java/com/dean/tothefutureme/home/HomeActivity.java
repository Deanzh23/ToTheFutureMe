package com.dean.tothefutureme.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.attention.model.AttentionModel;
import com.dean.tothefutureme.attention.view.AttentionListActivity;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityHomeBinding;
import com.dean.tothefutureme.letter.model.LetterModel;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.push.TTFMPushReceiver;
import com.dean.tothefutureme.timeline.view.TimeLineFragment;
import com.dean.tothefutureme.utils.TokenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * HomeActivity
 * <p>
 * Created by dean on 2017/12/3.
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends ConvenientActivity<ActivityHomeBinding> {

    private FragmentManager fragmentManager;
    private TimeLineFragment timeLineFragment;

    private static Boolean isExit = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // 头像数据更新广播
            if (AppConfig.BROADCAST_RECEIVER_USER_UPDATE.equals(action)) {
                if (timeLineFragment != null)
                    timeLineFragment.updateAvatar();
            }
            // 信件已读更新广播接收
            else if (AppConfig.BROADCAST_RECEIVER_DATA_UPDATE.equals(action)) {
                LetterModel letterModel = (LetterModel) intent.getSerializableExtra(LetterModel.class.getSimpleName());
                if (timeLineFragment != null)
                    timeLineFragment.updateLetterRead(letterModel);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFragments();
        // 启动个推推送
        TTFMApplication.startYunBaPush();
        // 注册信件已读更新广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConfig.BROADCAST_RECEIVER_LETTER_READ_UPDATE);
        intentFilter.addAction(AppConfig.BROADCAST_RECEIVER_DATA_UPDATE);
        registerReceiver(receiver, intentFilter);

        // 更新关注数据
        updateAttentionData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(this);

        // 从推送进入，重新读取时间轴数据
        if (getIntent().getBooleanExtra(TTFMPushReceiver.class.getSimpleName(), false) && timeLineFragment != null)
            timeLineFragment.loadData();
    }

    /**
     * 加载fragments
     */
    private void loadFragments() {
        fragmentManager = getSupportFragmentManager();
        timeLineFragment = TimeLineFragment.getInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contentLayout, timeLineFragment);
        fragmentTransaction.commit();
    }

    /**
     * 更新关注数据
     */
    private void updateAttentionData() {
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
                            try {
                                JSONObject response = new JSONObject(s);
                                String code = response.getString("code");
                                if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                    JSONArray array = response.getJSONArray("data");
                                    List<AttentionModel> attentionModels = JSONUtil.jsonArray2List(array, AttentionModel.class);

                                    // 将关注保存到数据库
                                    DatabaseUtil.deleteAll(AttentionModel.class, null);
                                    if (attentionModels != null && attentionModels.size() > 0) {
                                        for (AttentionModel attentionModel : attentionModels)
                                            DatabaseUtil.saveOrUpdate(attentionModel);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int i) {
                        }

                        @Override
                        public void onTokenFailure() {
                            HomeActivity.this.runOnUiThread(() -> TokenUtils.loginAgain(HomeActivity.this));
                        }

                        @Override
                        public void onEnd() {
                        }
                    });
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            exitBy2Click();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出
     */
    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            ToastUtil.showToast(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else
            System.exit(0);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);

        super.onDestroy();
    }
}
