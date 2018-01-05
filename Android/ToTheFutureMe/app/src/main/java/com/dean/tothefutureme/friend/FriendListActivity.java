package com.dean.tothefutureme.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.databinding.ActivityFriendListBinding;

/**
 * 好友Activity
 * <p>
 * Created by dean on 2018/1/5.
 */
@ContentView(R.layout.activity_friend_list)
public class FriendListActivity extends ConvenientActivity<ActivityFriendListBinding> {

    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle("好友列表");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> FriendListActivity.this.finish());

        loadData();
    }

    private void loadData() {
        viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.messageLayout);
    }

}
