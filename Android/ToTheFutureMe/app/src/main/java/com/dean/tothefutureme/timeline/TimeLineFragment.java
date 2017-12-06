package com.dean.tothefutureme.timeline;

import android.app.Activity;
import android.content.Context;

import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.FragmentTimeLineBinding;
import com.dean.tothefutureme.me.MeFragment;

/**
 * 时间轴Fragment
 * <p>
 * Created by dean on 2017/12/3.
 */
@ContentView(R.layout.fragment_time_line)
public class TimeLineFragment extends ConvenientFragment<FragmentTimeLineBinding> {

    private Activity activity;

    private static TimeLineFragment instance;

    public static TimeLineFragment getInstance() {
        if (instance == null)
            instance = new TimeLineFragment();

        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

}
