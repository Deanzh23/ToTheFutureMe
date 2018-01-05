package com.dean.tothefutureme.friend;

import android.content.Context;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.auth.model.AuthModel;
import com.dean.tothefutureme.databinding.AdapterFriendBinding;

import java.util.List;

/**
 * 好友Adapter
 * <p>
 * Created by dean on 2018/1/5.
 */
public class FriendAdapter extends ConvenientAdapter<AdapterFriendBinding> {

    private Context context;
    private List<AuthModel> authModels;

    public FriendAdapter(Context context, List<AuthModel> authModels) {
        this.context = context;
        this.authModels = authModels;
    }

    @Override
    public int setItemLayoutId() {
        return R.layout.adapter_friend;
    }

    @Override
    public void setItemView(AdapterFriendBinding adapterFriendBinding, int i) {

    }

    @Override
    public int getCount() {
        return authModels == null ? 0 : authModels.size();
    }

    @Override
    public Object getItem(int position) {
        return authModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
