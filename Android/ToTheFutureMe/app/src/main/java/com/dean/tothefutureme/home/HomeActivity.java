package com.dean.tothefutureme.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.fragment.ConvenientFragment;
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
public class HomeActivity extends ConvenientActivity<ActivityHomeBinding> {

    private FragmentManager fragmentManager;

    private List<ConvenientFragment> fragments = new ArrayList<>();
    private TimeLineFragment timeLineFragment;
    private MeFragment meFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        timeLineFragment = TimeLineFragment.getInstance();
        meFragment = MeFragment.getInstance();
        fragments.add(timeLineFragment);
        fragments.add(meFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static Boolean isExit = false;

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
}
