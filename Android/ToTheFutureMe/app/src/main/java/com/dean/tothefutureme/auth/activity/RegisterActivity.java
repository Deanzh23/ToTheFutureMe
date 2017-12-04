package com.dean.tothefutureme.auth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.dean.android.framework.convenient.activity.ConvenientCameraActivity;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityRegisterBinding;
import com.dean.tothefutureme.main.TTFMApplication;

/**
 * 用户注册Activity
 * <p>
 * Created by dean on 2017/12/4.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends ConvenientCameraActivity<ActivityRegisterBinding> {

    private AlertDialog selectImageDialog;

    private String avatarImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.setAuthModel(TTFMApplication.getAuthModel());
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
        selectImageDialog = builder.create();
        selectImageDialog.show();
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
