package com.dean.tothefutureme.timeline.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.android.framework.convenient.database.Selector;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.attention.view.AttentionListActivity;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.FragmentTimeLineBinding;
import com.dean.tothefutureme.letter.model.LetterModel;
import com.dean.tothefutureme.letter.view.LocalLetterListActivity;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.me.MeActivity;
import com.dean.tothefutureme.utils.TokenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 时间轴Fragment
 * <p>
 * Created by dean on 2017/12/3.
 */
@ContentView(R.layout.fragment_time_line)
public class TimeLineFragment extends ConvenientFragment<FragmentTimeLineBinding> {

    private AppCompatActivity activity;

    private TimeLineAdapter timeLineAdapter;

    @SuppressLint("StaticFieldLeak")
    private static TimeLineFragment instance;

    private Handler handler = new Handler();

    public static TimeLineFragment getInstance() {
        if (instance == null)
            instance = new TimeLineFragment();

        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());
        BitmapUtil.imageLoader(viewDataBinding.avatarImageView, AppConfig.BASE_URL + TTFMApplication.getAuthModel().getAvatarUrl(), AppConfig.APP_IMAGE_PAT,
                false);
        loadData();
    }

    /**
     * 读取数据
     */
    public void loadData() {
        viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.messageLayout);

        new Thread(() -> {
            List<String> urlParams = new ArrayList<>();
            urlParams.add(TTFMApplication.getAuthModel().getToken());
            urlParams.add(TTFMApplication.getAuthModel().getUsername());
            urlParams.add("0");
            urlParams.add("1000");

            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.LETTER_LOAD, null, urlParams, (Map<String, String>) null,
                    new OnHttpConnectionListener() {
                        @Override
                        public void onSuccess(String s) {
                            try {
                                JSONObject response = new JSONObject(s);
                                String code = response.getString("code");

                                if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                    JSONArray data = response.getJSONArray("data");
                                    List<LetterModel> letterModels = JSONUtil.jsonArray2List(data, LetterModel.class);

                                    if (!SetUtil.isEmpty(letterModels)) {
                                        // 绘制界面
                                        handler.post(() -> setLetterData(letterModels));

                                        // 存储到数据库
                                        for (LetterModel letterModel : letterModels)
                                            DatabaseUtil.saveOrUpdate(letterModel);
                                    }
                                } else
                                    onError(-2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                onError(-1);
                            }
                        }

                        @Override
                        public void onError(int i) {
                            handler.post(() -> ToastUtil.showToast(activity, "获取信件失败 " + i));

                            // 读取本地数据库
                            Selector selector = new Selector("userId", "=", TTFMApplication.getAuthModel().getUsername());
                            List<LetterModel> letterModels = DatabaseUtil.findAll(LetterModel.class, selector);
                            // 绘制界面
                            handler.post(() -> setLetterData(letterModels));
                        }

                        @Override
                        public void onTokenFailure() {
                            handler.post(() -> TokenUtils.loginAgain(activity));
                        }

                        @Override
                        public void onEnd() {
                            handler.post(() -> viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.messageLayout));
                        }
                    });
        }).start();
    }

    private void setLetterData(List<LetterModel> letterModels) {
        if (SetUtil.isEmpty(letterModels)) {
            viewDataBinding.timeLineListView.setVisibility(View.GONE);
            viewDataBinding.messageLineTextView.setVisibility(View.VISIBLE);
        } else {
            viewDataBinding.imageBottomView.setVisibility(View.VISIBLE);
            viewDataBinding.messageLineTextView.setVisibility(View.GONE);
        }

        if (timeLineAdapter == null) {
            timeLineAdapter = new TimeLineAdapter(activity, letterModels);
            viewDataBinding.timeLineListView.setAdapter(timeLineAdapter);
        } else
            timeLineAdapter.update(letterModels);

        viewDataBinding.imageBottomView.setVisibility(SetUtil.isEmpty(letterModels) ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * 跳转到本地信件列表
     */
    @OnClick(R.id.jump2LocalLetterImageView)
    public void jump2LocalLetterList() {
        startActivity(new Intent(activity, LocalLetterListActivity.class));
    }

    /**
     * 跳转到用户信息编辑
     */
    @OnClick(R.id.avatarImageView)
    public void jump2UserInfoEdit() {
        startActivity(new Intent(activity, MeActivity.class));
    }

    /**
     * 跳转到好友列表
     */
    @OnClick(R.id.jump2FriendImageView)
    public void jump2FriendList() {
        startActivity(new Intent(activity, AttentionListActivity.class));
    }

    /**
     * 更新信件已读
     *
     * @param letterModel
     */
    public void updateLetterRead(LetterModel letterModel) {
        if (timeLineAdapter != null && letterModel != null) {
            List<LetterModel> letterModels = timeLineAdapter.getLetterModels();

            int count = letterModels == null ? 0 : letterModels.size();
            if (count <= 0)
                return;

            for (LetterModel tempLetterModel : letterModels) {
                if (tempLetterModel.getLetterId().equals(letterModel.getLetterId())) {
                    tempLetterModel.setIsRead(1);

                    Log.d(TimeLineAdapter.class.getSimpleName(), "[timeLineAdapter.notifyDataSetChanged()]");
                    timeLineAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    /**
     * 更新用户头像信息数据
     */
    public void updateAvatar() {
        BitmapUtil.imageLoader(viewDataBinding.avatarImageView, AppConfig.BASE_URL + TTFMApplication.getAuthModel().getAvatarUrl(), AppConfig.APP_IMAGE_PAT,
                false);
    }

}
