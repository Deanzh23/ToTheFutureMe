package com.dean.tothefutureme.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.tothefutureme.auth.activity.LoginActivity;

/**
 * token工具类
 * <p>
 * Created by dean on 2017/12/24.
 */
public class TokenUtils {

    /**
     * 重新登陆
     *
     * @param context
     */
    public static void loginAgain(Context context) {
        ToastUtil.showToast(context, "登陆已过期，请重新登陆", Toast.LENGTH_LONG);

        context.startActivity(new Intent(context, LoginActivity.class));
        ConvenientApplication.killHistoryActivity(LoginActivity.class.getSimpleName());
    }

}
