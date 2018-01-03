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
    private Map<String, Integer> receiveColorDrawables = new HashMap<>();

    public TimeLineAdapter(Context context, List<LetterModel> letterModels) {
        this.context = context;
        this.letterModels = letterModels;

        colorDrawableArray = new int[3];
        colorDrawableArray[0] = R.drawable.time_line_1;
        colorDrawableArray[1] = R.drawable.time_line_2;
        colorDrawableArray[2] = R.drawable.time_line_3;
        colorTextArray = new int[3];
        colorTextArray[0] = R.color.colorTimeLine1;
        colorTextArray[1] = R.color.colorTimeLine2;
        colorTextArray[2] = R.color.colorTimeLine3;
        colorBackgroundArray = new int[3];
        colorBackgroundArray[0] = R.drawable.shape_time_line_1_content;
        colorBackgroundArray[1] = R.drawable.shape_time_line_2_content;
        colorBackgroundArray[2] = R.drawable.shape_time_line_3_content;
    }

    private int colorCount;
    private int[] colorDrawableArray;
    private int[] colorTextArray;
    private int[] colorBackgroundArray;

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
        adapterTimeLineBinding.dateTextView.setVisibility(receiveDates.get(receiveDate) == i ? View.VISIBLE : View.INVISIBLE);
        // 设置颜色
        int colorIndex;
        if (receiveColorDrawables.containsKey(receiveDate))
            colorIndex = receiveColorDrawables.get(receiveDate);
        else {
            colorIndex = (++colorCount) % 3;
            receiveColorDrawables.put(receiveDate, colorIndex);
        }
        adapterTimeLineBinding.lineImageView.setBackgroundResource(colorDrawableArray[colorIndex]);
        adapterTimeLineBinding.dateTextView.setTextColor(context.getResources().getColor(colorTextArray[colorIndex]));
        adapterTimeLineBinding.senderNicknameTextView.setTextColor(context.getResources().getColor(colorTextArray[colorIndex]));
        adapterTimeLineBinding.sendDateTimeTextView.setTextColor(context.getResources().getColor(colorTextArray[colorIndex]));
        adapterTimeLineBinding.contentTextView.setBackgroundResource(colorBackgroundArray[colorIndex]);

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
