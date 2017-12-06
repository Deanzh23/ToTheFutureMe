package com.dean.tothefutureme.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityHomeBinding;
import com.dean.tothefutureme.me.MeFragment;
import com.dean.tothefutureme.timeline.TimeLineFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * HomeActivity
 * <p>
 * Created by dean on 2017/12/3.
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends ConvenientActivity<ActivityHomeBinding> implements RadioGroup.OnCheckedChangeListener {

    private FragmentManager fragmentManager;

    private List<ConvenientFragment> fragments = new ArrayList<>();
    private TimeLineFragment timeLineFragment;
    private MeFragment meFragment;

    private static Boolean isExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFragments();
        viewDataBinding.bottomTabLayout.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    /**
     * 加载fragments
     */
    private void loadFragments() {
        timeLineFragment = TimeLineFragment.getInstance();
        meFragment = MeFragment.getInstance();
        fragments.add(timeLineFragment);
        fragments.add(meFragment);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contentLayout, timeLineFragment);
        fragmentTransaction.hide(timeLineFragment);
        fragmentTransaction.add(R.id.contentLayout, meFragment);
        fragmentTransaction.hide(meFragment);
        fragmentTransaction.commit();

        switchFragment(0);
    }

    /**
     * 通过下标切换显示Fragment
     *
     * @param index
     */
    private void switchFragment(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        int size = fragments == null ? 0 : fragments.size();
        for (int i = 0; i < size; i++)
            if (i != index)
                fragmentTransaction.hide(fragments.get(i));

        fragmentTransaction.show(fragments.get(index));
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出
     */
    private void exitBy2Click() {
        Timer tExit;
        if (isExit == false) {
            isExit = true;
            ToastUtil.showToast(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else
            System.exit(0);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (fragmentManager == null)
            return;

        int index = 0;
        switch (checkedId) {
            case R.id.timeLineRadioButton:
                index = 0;
                break;
            case R.id.meRadioButton:
                index = 1;
                break;
        }

        switchFragment(index);
    }

}
