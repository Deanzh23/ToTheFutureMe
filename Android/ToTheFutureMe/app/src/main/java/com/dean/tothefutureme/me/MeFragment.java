package com.dean.tothefutureme.me;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.auth.activity.AuthSettingActivity;
import com.dean.tothefutureme.auth.model.AuthModel;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.FragmentMeBinding;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.utils.TokenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的 Fragment
 * <p>
 * Created by dean on 2017/12/6.
 */
@ContentView(R.layout.fragment_me)
public class MeFragment extends ConvenientFragment<FragmentMeBinding> implements Toolbar.OnMenuItemClickListener {

    private AppCompatActivity activity;
    @SuppressLint("StaticFieldLeak")
    private static MeFragment instance;

    private ProgressDialog waitDialog;

    private String avatarImagePath;

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
        inflater.inflate(R.menu.menu_me, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewDataBinding.toolbar.setTitle("我的");
        activity.setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.inflateMenu(R.menu.menu_me);
        viewDataBinding.toolbar.setOnMenuItemClickListener(this);

        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());
        BitmapUtil.imageLoader(viewDataBinding.avatarImageView, AppConfig.BASE_URL + TTFMApplication.getAuthModel().getAvatarUrl(), AppConfig.APP_IMAGE_PAT,
                false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddLetter:
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
                break;
            case R.id.menuSetting:
                startActivity(new Intent(activity, AuthSettingActivity.class));
                break;
        }

        return true;
    }

    /**
     * 选取图片作为头像
     */
    @OnClick(R.id.avatarImageView)
    public void selectImage() {
        if (!TTFMApplication.getAuthModel().isEditModel())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("选取方式");
        builder.setNegativeButton("相机", (dialog, which) -> BitmapUtil.openSystemCamera(activity, AppConfig.APP_IMAGE_PAT, "temp.png"));
        builder.setNeutralButton("相册", (dialog, which) -> BitmapUtil.openSystemPhotoAlbum(activity));
        builder.create().show();
    }

    /**
     * 上传用户信息
     */
    private void uploadAuth() {
        List<String> urlParams = new ArrayList<>();
        urlParams.add(TTFMApplication.getAuthModel().getToken());

        ConvenientHttpConnection connection = new ConvenientHttpConnection();
        connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.AUTH_UPLOAD, null, urlParams,
                JSONUtil.object2Json(TTFMApplication.getAuthModel()).toString(), new HttpConnectionListener() {

                    @Override
                    public void onSuccess(String s) {
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
                                onError(-2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onError(-1);
                        }
                    }

                    @Override
                    public void onError(int i) {
                        handler.post(() -> {
                            if (waitDialog != null && waitDialog.isShowing())
                                waitDialog.dismiss();

                            ToastUtil.showToast(activity, "更新失败 " + i);
                        });
                    }

                    @Override
                    public void onTokenFailure() {
                        handler.post(() -> {
                            if (waitDialog != null && waitDialog.isShowing())
                                waitDialog.dismiss();
                            TokenUtils.loginAgain(activity);
                        });
                    }

                    @Override
                    public void onEnd() {
                    }
                });
    }

    /**
     * 设置性别
     */
    @OnClick(R.id.genderTextView)
    public void setGender() {
        if (!TTFMApplication.getAuthModel().isEditModel())
            return;

        String[] genders = new String[]{"女", "男"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("您的性别是？");
        builder.setItems(genders, (dialog, which) -> TTFMApplication.getAuthModel().setGenderCode(which));
        builder.create().show();
    }

    private Date birthday;

    /**
     * 设置生日
     */
    @OnClick(R.id.birthdayTextView)
    public void setBirthday() {
        if (!TTFMApplication.getAuthModel().isEditModel())
            return;

        if (TTFMApplication.getAuthModel().getBirthday() <= 0)
            birthday = new Date();
        else
            birthday = new Date(TTFMApplication.getAuthModel().getBirthday());

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, (view, year, month, dayOfMonth) -> {

            birthday.setYear(year - 1900);
            birthday.setMonth(month);
            birthday.setDate(dayOfMonth);

            TTFMApplication.getAuthModel().setBirthday(birthday.getTime());
        }, 1900 + birthday.getYear(), birthday.getMonth(), birthday.getDate());
        datePickerDialog.show();
    }

    public void albumResult(Intent intent) {
        avatarImagePath = BitmapUtil.intent2ImagePath(activity, intent);
        setImage2Avatar();
    }

    public void cameraResult(Intent intent) {
        avatarImagePath = AppConfig.APP_IMAGE_PAT + "/temp.png";
        setImage2Avatar();
    }

    /**
     * 设置图片到头像控件
     */
    private void setImage2Avatar() {
        if (!TextUtils.isEmpty(avatarImagePath))
            BitmapUtil.setBitmap2ViewOnImageBitmap(activity, viewDataBinding.avatarImageView, avatarImagePath, true, null);
        else
            ToastUtil.showToast(activity, "图片未找到");
    }

}
