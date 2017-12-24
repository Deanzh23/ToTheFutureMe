package com.dean.tothefutureme.timeline.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.AdapterTimeLineBinding;
import com.dean.tothefutureme.letter.model.LetterModel;
import com.dean.tothefutureme.letter.view.LetterEditActivity;
import com.dean.tothefutureme.utils.DateTimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 时间轴Adapter
 * <p>
 * Created by dean on 2017/12/6.
 */
public class TimeLineAdapter extends ConvenientAdapter<AdapterTimeLineBinding> {

    private Context context;

    private List<LetterModel> letterModels;
    private Map<String, Integer> receiveDates = new HashMap<>();

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

        // 将第一个显示的接收日期和下标记录
        String receiveDate = DateTimeUtils.getDate2String(letterModel.getReceiveDateTime());
        if (!receiveDates.containsKey(receiveDate))
            receiveDates.put(receiveDate, i);

        // 如果已经显示了"接收日期"，则不再显示相同的接收日期
        if (receiveDates.get(receiveDate) != i)
            adapterTimeLineBinding.dateTextView.setVisibility(View.INVISIBLE);
        // 设置发件人头像
        BitmapUtil.imageLoader(adapterTimeLineBinding.senderAvatarImageView, AppConfig.BASE_URL + letterModel.getSenderAvatarUrl(), AppConfig.APP_IMAGE_PAT,
                false);

        adapterTimeLineBinding.getRoot().setOnClickListener(null);
        adapterTimeLineBinding.contentTextView.setOnClickListener(v -> {
            // 跳转到信件编辑界面（只能查看不能编辑）
            Intent intent = new Intent(context, LetterEditActivity.class);
            intent.putExtra(LetterModel.class.getSimpleName(), letterModel);
            intent.putExtra(TimeLineFragment.class.getSimpleName(), true);
            context.startActivity(intent);
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

    public List<LetterModel> getLetterModels() {
        return letterModels;
    }

    @Override
    public void notifyDataSetChanged() {
        // 清理记录"接收日期"的集合
        receiveDates.clear();

        super.notifyDataSetChanged();
    }
}
