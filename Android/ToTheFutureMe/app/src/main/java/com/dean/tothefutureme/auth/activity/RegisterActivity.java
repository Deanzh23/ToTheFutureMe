package com.dean.tothefutureme.auth.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.dean.android.framework.convenient.activity.ConvenientCameraActivity;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
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
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityRegisterBinding;
import com.dean.tothefutureme.main.TTFMApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用户注册Activity
 * <p>
 * Created by dean on 2017/12/4.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends ConvenientCameraActivity<ActivityRegisterBinding> {

    private ProgressDialog waitDialog;

    private String avatarImagePath;

    private Timer timer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TTFMApplication.getAuthModel().setPassword("");
        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 开始验证码计时器
        startTimerTask();
    }

    /**
     * 选取图片作为头像
     */
    @OnClick(R.id.avatarImageView)
    public void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选取方式");
//        BitmapUtil.openSystemCamera(this, AppConfig.APP_IMAGE_PAT, "temp.png")
        builder.setNegativeButton("相机", (dialog, which) -> BitmapUtil.openSystemCamera(this, AppConfig.APP_IMAGE_PAT, "temp.png"));
        builder.setNeutralButton("相册", (dialog, which) -> BitmapUtil.openSystemPhotoAlbum(this));
        builder.create().show();
    }

    /**
     * 重新发送验证码
     */
    @OnClick(R.id.sendVerificationCodeAgainButton)
    public void sendVerificationCodeAgain() {
        if (!(Boolean) viewDataBinding.sendVerificationCodeAgainButton.getTag())
            return;

        // 开始计时器
        startTimerTask();

        new Thread(() -> {
            Map<String, String> bodyParams = new LinkedHashMap<>();
            bodyParams.put("username", TTFMApplication.getAuthModel().getUsername());

            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.AUTH_SEND_VERIFICATION_CODE_AGAIN, null, null, bodyParams,
                    new HttpConnectionListener() {
                        @Override
                        public void success(String s) {
                            try {
                                JSONObject response = new JSONObject(s);
                                String code = response.getString("code");

                                // 重新发送验证码成功
                                if (AppConfig.RESPONSE_SUCCESS.equals(code))
                                    ToastUtil.showToast(RegisterActivity.this, "验证码已发送到您的邮箱", Toast.LENGTH_LONG);
                                else
                                    error(-2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                error(-1);
                            }
                        }

                        @Override
                        public void error(int i) {
                            handler.post(() -> ToastUtil.showToast(RegisterActivity.this, "发送验证码失败"));
                        }

                        @Override
                        public void end() {
                            // 重新计时
                        }
                    });
        }).start();
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

        new Thread(() -> {
            // 上传头像
            if (!TextUtils.isEmpty(avatarImagePath)) {
                List<Object> urlParams = new ArrayList<>();
                urlParams.add(AppConfig.IMAGE_TYPE_AVATAR);

                ConvenientHttpConnection connection = new ConvenientHttpConnection();
                connection.sendFile(AppConfig.BASE_URL + AppConfig.FILE, urlParams, new File(avatarImagePath), new HttpConnectionListener() {
                    @Override
                    public void success(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            String code = response.getString("code");
                            if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                // 设置头像
                                String url = response.getJSONObject("data").getString("url");
                                TTFMApplication.getAuthModel().setAvatarUrl(url);
                            }
                        } catch (JSONException e) {
                            handler.post(() -> ToastUtil.showToast(RegisterActivity.this, "上传头像失败"));
                        }

                        // 注册用户信息
                        new Thread(() -> registerUserInfo()).start();
                    }

                    @Override
                    public void error(int i) {
                        handler.post(() -> {
                            waitDialog.dismiss();
                            ToastUtil.showToast(RegisterActivity.this, i + "", Toast.LENGTH_LONG);
                        });
                    }

                    @Override
                    public void end() {
                    }
                });
            } else {
                // 正式注册用户
                registerUserInfo();
            }
        }).start();
    }

    /**
     * 注册用户信息
     */
    private void registerUserInfo() {
        ConvenientHttpConnection registerConnection = new ConvenientHttpConnection();
        registerConnection.sendHttpPost(AppConfig.BASE_URL + AppConfig.AUTH_REGISTER, null, null,
                JSONUtil.object2Json(TTFMApplication.getAuthModel()).toString(), new HttpConnectionListener() {
                    @Override
                    public void success(String s) {
                        handler.post(() -> {
                            try {
                                JSONObject response = new JSONObject(s);
                                String code = response.getString("code");
                                if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                    // 注册成功
                                    ToastUtil.showToast(RegisterActivity.this, "注册成功");
                                    RegisterActivity.this.finish();
                                } else if ("402".equals(code)) {
                                    // 验证码失效
                                    ToastUtil.showToast(RegisterActivity.this, "验证码失效");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void error(int i) {
                        handler.post(() -> ToastUtil.showToast(RegisterActivity.this, "注册失败 " + i));
                    }

                    @Override
                    public void end() {
                        handler.post(() -> waitDialog.dismiss());
                    }
                });
    }

    @Override
    protected void albumResult(Intent intent) {
        avatarImagePath = BitmapUtil.intent2ImagePath(this, intent);
        setImage2Avatar();
    }

    @Override
    protected void cameraResult(Intent intent) {
        avatarImagePath = AppConfig.APP_IMAGE_PAT + "/temp.png";
    }

    /**
     * 设置图片到头像控件
     */
    private void setImage2Avatar() {
        if (!TextUtils.isEmpty(avatarImagePath))
            BitmapUtil.setBitmap2ViewOnImageBitmap(this, viewDataBinding.avatarImageView, avatarImagePath, false, null);
        else
            ToastUtil.showToast(this, "图片未找到");
    }

    /**
     * 开始计时器
     */
    private void startTimerTask() {
        viewDataBinding.sendVerificationCodeAgainButton.setTag(false);
        viewDataBinding.sendVerificationCodeAgainButton.setBackgroundResource(R.drawable.ic_dean_button_gray_bg);

        TimerTask task = new TimerTask() {

            int time = 60;

            @Override
            public void run() {
                runOnUiThread(() -> {
                    viewDataBinding.sendVerificationCodeAgainButton.setText("重新发送(" + (time < 10 ? "0" : "") + time + "秒)");

                    if (time-- <= 0) {
                        viewDataBinding.sendVerificationCodeAgainButton.setText("重新发送(60秒)");
                        viewDataBinding.sendVerificationCodeAgainButton.setBackgroundResource(R.drawable.ic_dean_button_blue_bg);
                        viewDataBinding.sendVerificationCodeAgainButton.setTag(true);

                        if (timer != null)
                            timer.cancel();
                    }
                });
            }
        };

        timer = new Timer(true);
        timer.schedule(task, 1000, 1000);
    }

}
