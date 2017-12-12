package com.dean.tothefutureme.me;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.auth.activity.LoginActivity;
import com.dean.tothefutureme.auth.model.AuthModel;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.FragmentMeBinding;
import com.dean.tothefutureme.main.TTFMApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的 Fragment
 * <p>
 * Created by dean on 2017/12/6.
 */
@ContentView(R.layout.fragment_me)
public class MeFragment extends ConvenientFragment<FragmentMeBinding> implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private AppCompatActivity activity;
    private static MeFragment instance;

    private ProgressDialog waitDialog;

    private Handler handler = new Handler();

    public static MeFragment getInstance() {
        if (instance == null)
            instance = new MeFragment();

        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewDataBinding.toolbar.setTitle("我的");
        activity.setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.inflateMenu(R.menu.menu_edit);
        viewDataBinding.toolbar.setOnMenuItemClickListener(this);

        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());

        viewDataBinding.exitLoginButton.setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        boolean isEditModel = !TTFMApplication.getAuthModel().isEditModel();
        TTFMApplication.getAuthModel().setEditModel(isEditModel);

        viewDataBinding.toolbar.getMenu().getItem(0).setIcon(isEditModel ? R.drawable.ic_menu_save : R.drawable.ic_menu_user_info_edit);
        viewDataBinding.nicknameEditText.setBackgroundResource(isEditModel ? R.drawable.ic_dean_text_blue_bg : android.R.color.white);
        viewDataBinding.genderTextView.setBackgroundResource(isEditModel ? R.drawable.ic_dean_text_blue_bg : android.R.color.white);
        viewDataBinding.birthdayTextView.setBackgroundResource(isEditModel ? R.drawable.ic_dean_text_blue_bg : android.R.color.white);

        ToastUtil.showToast(activity, isEditModel ? "开启编辑" : "保存信息");

        // 保存信息
        if (!isEditModel) {
            waitDialog = ConvenientProgressDialog.getInstance(activity, "正在上传...", false);
            waitDialog.show();

            new Thread(() -> uploadAuth()).start();
        }

        return true;
    }

    /**
     * 上传用户信息
     */
    private void uploadAuth() {
        ConvenientHttpConnection connection = new ConvenientHttpConnection();
        connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.AUTH_UPLOAD, null, null,
                JSONUtil.object2Json(TTFMApplication.getAuthModel()).toString(), new HttpConnectionListener() {

                    @Override
                    public void success(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            String code = response.getString("code");

                            if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                AuthModel responseAuthMode = JSONUtil.json2Object(response.getJSONObject("data"), AuthModel.class);

                                TTFMApplication.getAuthModel().setNickname(responseAuthMode.getNickname());
                                TTFMApplication.getAuthModel().setGenderCode(responseAuthMode.getGenderCode());
                                TTFMApplication.getAuthModel().setBirthday(responseAuthMode.getBirthday());

                                // 更新本地数据库用户信息
                                DatabaseUtil.saveOrUpdate(TTFMApplication.getAuthModel());

                                handler.post(() -> {
                                    if (waitDialog != null && waitDialog.isShowing())
                                        waitDialog.dismiss();

                                    ToastUtil.showToast(activity, "更新成功");
                                });
                            } else
                                error(-2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            error(-1);
                        }
                    }

                    @Override
                    public void error(int i) {
                        handler.post(() -> {
                            if (waitDialog != null && waitDialog.isShowing())
                                waitDialog.dismiss();

                            ToastUtil.showToast(activity, "更新失败 " + i);
                        });
                    }

                    @Override
                    public void end() {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 退出登陆
            case R.id.exitLoginButton:
                startActivity(new Intent(activity, LoginActivity.class));
                TTFMApplication.killHistoryActivity(LoginActivity.class.getSimpleName());
                break;
        }
    }
}
