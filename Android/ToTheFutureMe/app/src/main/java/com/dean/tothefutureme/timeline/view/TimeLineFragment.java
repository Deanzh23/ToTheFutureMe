package com.dean.tothefutureme.timeline.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.FragmentTimeLineBinding;
import com.dean.tothefutureme.timeline.model.LetterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间轴Fragment
 * <p>
 * Created by dean on 2017/12/3.
 */
@ContentView(R.layout.fragment_time_line)
public class TimeLineFragment extends ConvenientFragment<FragmentTimeLineBinding> {

    private AppCompatActivity activity;

    private TimeLineAdapter timeLineAdapter;
    private List<LetterModel> letterModels;

    private static TimeLineFragment instance;

    public static TimeLineFragment getInstance() {
        if (instance == null)
            instance = new TimeLineFragment();

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

        viewDataBinding.toolbar.setTitle("时间轴");
        activity.setSupportActionBar(viewDataBinding.toolbar);

        loadData();
    }

    private void loadData() {
        /**
         * debug
         */
        letterModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LetterModel letterModel = new LetterModel();
            letterModels.add(letterModel);
        }

        timeLineAdapter = new TimeLineAdapter(activity, letterModels);
        viewDataBinding.timeLineListView.setAdapter(timeLineAdapter);
    }

}