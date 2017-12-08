package com.dean.tothefutureme.letter.view;

import android.content.Context;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.AdapterLocalLetterBinding;
import com.dean.tothefutureme.letter.model.LetterModel;

import java.util.List;

/**
 * 本地信件Adapter
 * <p>
 * Created by dean on 2017/12/8.
 */
public class LocalLetterAdapter extends ConvenientAdapter<AdapterLocalLetterBinding> {

    private Context context;
    private List<LetterModel> letterModels;

    public LocalLetterAdapter(Context context, List<LetterModel> letterModels) {
        this.context = context;
        this.letterModels = letterModels;
    }

    @Override
    public int setItemLayoutId() {
        return R.layout.adapter_local_letter;
    }

    @Override
    public void setItemView(AdapterLocalLetterBinding adapterLocalLetterBinding, int i) {

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
