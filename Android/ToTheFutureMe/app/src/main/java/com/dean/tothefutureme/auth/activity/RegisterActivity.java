package com.dean.tothefutureme.auth.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.dean.android.framework.convenient.activity.ConvenientCameraActivity;
import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityRegisterBinding;
import com.dean.tothefutureme.home.HomeActivity;
import com.dean.tothefutureme.main.TTFMApplication;

import java.util.Date;

/**
 * 用户注册Activity
 * <p>
 * Created by dean on 2017/12/4.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends ConvenientCameraActivity<ActivityRegisterBinding> {

    private ProgressDialog waitDialog;

    private String avatarImagePath;

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

    /**
     * 选取图片作为头像
     */
    @OnClick(R.id.avatarImageView)
    public void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选取方式");
        builder.setNegativeButton("相机", (dialog, which) -> {
            BitmapUtil.openSystemCamera(this, TTFMApplication.APP_IMAGE_PAT, "temp.png");
        });
        builder.setNeutralButton("相册", (dialog, which) -> {
            BitmapUtil.openSystemPhotoAlbum(this);
        });
        builder.create().show();
    }

    /**
     * 设置性别
     */
    @OnClick(R.id.genderTextView)
    public void setGender() {
        String[] genders = new String[]{"女", "男"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您的性别是？");
        builder.setItems(genders, (dialog, which) -> {
            TTFMApplication.getAuthModel().setGenderCode(which);
        });
        builder.create().show();
    }

    /**
     * 设置生日
     */
    @OnClick(R.id.birthdayTextView)
    public void setBirthday() {
        Date date = new Date();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            viewDataBinding.birthdayTextView.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
        }, 1900 + date.getYear(), date.getMonth(), date.getDate());
        datePickerDialog.show();
    }

    /**
     * 注册
     */
    @OnClick(R.id.registerButton)
    public void register() {
        // 必填项检查
        if (TextUtils.isEmpty(TTFMApplication.getAuthModel().getVerificationCode())) {
            ToastUtil.showToast(this, "请填写验证码");
            return;
        }
        if (TextUtils.isEmpty(TTFMApplication.getAuthModel().getPassword())) {
            ToastUtil.showToast(this, "请填写密码");
            return;
        }
        if (TextUtils.isEmpty(TTFMApplication.getAuthModel().getNickname())) {
            ToastUtil.showToast(this, "请填写昵称");
            return;
        }

        // 开始提交
        waitDialog = ConvenientProgressDialog.getInstance(this, "正在注册用户，请稍后...", false);
        waitDialog.show();

        /**
         * debug
         */
        handler.postDelayed(() -> {
            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
            ConvenientApplication.killHistoryActivity(HomeActivity.class.getSimpleName());
        }, 3000);
    }

    @Override
    protected void albumResult(Intent intent) {
        setImage2Avatar(intent);
    }

    @Override
    protected void cameraResult(Intent intent) {
        setImage2Avatar(intent);
    }

    /**
     * 设置图片到头像控件
     *
     * @param intent
     */
    private void setImage2Avatar(Intent intent) {
        avatarImagePath = BitmapUtil.intent2ImagePath(this, intent);

        if (!TextUtils.isEmpty(avatarImagePath))
            BitmapUtil.setBitmap2ViewOnImageBitmap(this, viewDataBinding.avatarImageView, avatarImagePath, false, null);
        else
            ToastUtil.showToast(this, "图片未找到");
    }

}
