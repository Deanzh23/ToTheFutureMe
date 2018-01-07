package com.dean.tothefutureme.attention.view;

import android.content.Context;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.attention.model.AttentionModel;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.AdapterAttentionBinding;

import java.util.List;

/**
 * 好友Adapter
 * <p>
 * Created by dean on 2018/1/5.
 */
public class AttentionAdapter extends ConvenientAdapter<AdapterAttentionBinding> {

    private Context context;
    private List<AttentionModel> attentionModels;

    public AttentionAdapter(Context context, List<AttentionModel> attentionModels) {
        this.context = context;
        this.attentionModels = attentionModels;
    }

    @Override
    public int setItemLayoutId() {
        return R.layout.adapter_attention;
    }

    @Override
    public void setItemView(AdapterAttentionBinding adapterAttentionBinding, int i) {
        AttentionModel attentionModel = attentionModels.get(i);
        adapterAttentionBinding.setAttentionModel(attentionModel);

        BitmapUtil.imageLoader(adapterAttentionBinding.avatarImageView, AppConfig.BASE_URL + attentionModel.getAvatarUrl(), AppConfig.APP_IMAGE_PAT,
                false);
    }

    @Override
    public int getCount() {
        return attentionModels == null ? 0 : attentionModels.size();
    }

    @Override
    public Object getItem(int position) {
        return attentionModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(List<AttentionModel> attentionModels) {
        this.attentionModels = attentionModels;
        notifyDataSetChanged();
    }
}
