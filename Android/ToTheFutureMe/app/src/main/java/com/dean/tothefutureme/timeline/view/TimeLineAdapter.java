package com.dean.tothefutureme.timeline.view;

import android.content.Context;
import android.view.View;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.AdapterTimeLineBinding;
import com.dean.tothefutureme.letter.model.LetterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间轴Adapter
 * <p>
 * Created by dean on 2017/12/6.
 */
public class TimeLineAdapter extends ConvenientAdapter<AdapterTimeLineBinding> {

    private Context context;

    private List<LetterModel> letterModels;

    private List<String> receiveDates = new ArrayList<>();

    public TimeLineAdapter(Context context, List<LetterModel> letterModels) {
        this.context = context;
        this.letterModels = letterModels;
    }

    @Override
    public int setItemLayoutId() {
        return R.layout.adapter_time_line;
    }

    @Override
    public void setItemView(AdapterTimeLineBinding adapterTimeLineBinding, int i) {
        LetterModel letterModel = letterModels.get(i);
        if (letterModel == null)
            return;

        adapterTimeLineBinding.setLetterModel(letterModel);

        // 设置发件人头像
        BitmapUtil.imageLoader(adapterTimeLineBinding.senderAvatarImageView, letterModel.getSenderAvatarUrl(), AppConfig.APP_IMAGE_PAT, false);

        receiveDates.add(letterModel.getReceiveDateName());

        adapterTimeLineBinding.getRoot().setOnClickListener(null);
        adapterTimeLineBinding.contentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 跳转到信件编辑界面（只能查看不能编辑）
            }
        });
    }

    @Override
    public int getCount() {
        return letterModels == null ? 0 : letterModels.size();
    }

    @Override
    public Object getItem(int position) {
        return letterModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(List<LetterModel> letterModels) {
        this.letterModels = letterModels;
        notifyDataSetChanged();
    }

}
