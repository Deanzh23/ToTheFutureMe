package com.dean.tothefutureme.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dean.android.framework.convenient.activity.ConvenientCameraActivity;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityMeBinding;

/**
 * 个人信息编辑Activity
 * <p>
 * Created by dean on 2018/1/5.
 */
@ContentView(R.layout.activity_me)
public class MeActivity extends ConvenientCameraActivity<ActivityMeBinding> {

    private FragmentManager fragmentManager;
    private MeFragment meFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        meFragment = MeFragment.getInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contentLayout, meFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void albumResult(Intent intent) {
        if (meFragment != null)
            meFragment.albumResult(intent);
    }

    @Override
    protected void cameraResult(Intent intent) {
        if (meFragment != null)
            meFragment.cameraResult(intent);
    }
}