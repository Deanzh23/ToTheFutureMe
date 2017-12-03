package com.dean.tothefutureme.auth;

import android.Manifest;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityLoginBinding;

/**
 * 登陆Activity
 * <p>
 * Created by dean on 2017/12/3.
 */
@Permission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
@ContentView(R.layout.activity_login)
public class LoginActivity extends ConvenientActivity<ActivityLoginBinding> {



}
