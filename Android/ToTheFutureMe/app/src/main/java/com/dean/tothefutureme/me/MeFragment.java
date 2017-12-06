package com.dean.tothefutureme.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.FragmentMeBinding;

/**
 * 我的 Fragment
 * <p>
 * Created by dean on 2017/12/6.
 */
@ContentView(R.layout.fragment_me)
public class MeFragment extends ConvenientFragment<FragmentMeBinding> {

    private AppCompatActivity activity;

    private static MeFragment instance;

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
    public void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewDataBinding.toolbar.setTitle("我的");
        activity.setSupportActionBar(viewDataBinding.toolbar);
    }

}
