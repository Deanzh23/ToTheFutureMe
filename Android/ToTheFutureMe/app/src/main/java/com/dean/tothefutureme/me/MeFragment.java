package com.dean.tothefutureme.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.FragmentMeBinding;
import com.dean.tothefutureme.main.TTFMApplication;

/**
 * 我的 Fragment
 * <p>
 * Created by dean on 2017/12/6.
 */
@ContentView(R.layout.fragment_me)
public class MeFragment extends ConvenientFragment<FragmentMeBinding> implements Toolbar.OnMenuItemClickListener {

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
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        boolean isEditModel = !TTFMApplication.getAuthModel().isEditModel();
        TTFMApplication.getAuthModel().setEditModel(isEditModel);

        viewDataBinding.toolbar.getMenu().getItem(0).setIcon(isEditModel ? R.drawable.ic_menu_save : R.drawable.ic_menu_user_info_edit);
        viewDataBinding.confirmPasswordLayout.setVisibility(isEditModel ? View.VISIBLE : View.GONE);

        ToastUtil.showToast(activity, isEditModel ? "开启编辑" : "保存信息");

        return true;
    }
}
