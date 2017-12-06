package com.dean.tothefutureme.me;

import android.app.Activity;
import android.content.Context;

import com.dean.android.framework.convenient.fragment.ConvenientFragment;
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

    private Activity activity;

    private static MeFragment instance;

    public static MeFragment getInstance() {
        if (instance == null)
            instance = new MeFragment();

        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
}
