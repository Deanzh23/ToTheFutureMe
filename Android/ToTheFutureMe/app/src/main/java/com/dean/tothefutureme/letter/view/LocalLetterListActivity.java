package com.dean.tothefutureme.letter.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.database.Selector;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.ActivityLocalLetterListBinding;
import com.dean.tothefutureme.letter.model.LetterModel;

import java.util.List;

/**
 * 本地信件列表Activity
 * <p>
 * Created by dean on 2017/12/6.
 */
@ContentView(R.layout.activity_local_letter_list)
public class LocalLetterListActivity extends ConvenientActivity<ActivityLocalLetterListBinding> implements Toolbar.OnMenuItemClickListener {

    private LocalLetterAdapter localLetterAdapter;

    private List<LetterModel> letterModels;

    private Handler handler = new Handler();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadDataFromDB();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle("本地信件列表");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> LocalLetterListActivity.this.finish());
        viewDataBinding.toolbar.setOnMenuItemClickListener(this);

        localLetterAdapter = new LocalLetterAdapter(this, letterModels);
        localLetterAdapter.setOnDataChangeUpdateListener(() -> loadDataFromDB());
        viewDataBinding.localLetterListView.setAdapter(localLetterAdapter);

        // 从数据库中读取数据
        loadDataFromDB();

        // 注册广播
        IntentFilter intentFilter = new IntentFilter(AppConfig.BROADCAST_RECEIVER_DATA_UPDATE);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // 跳转信件编写界面
        startActivity(new Intent(LocalLetterListActivity.this, LetterEditActivity.class));
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    /**
     * 从本地数据库中读取信件数据
     */
    private void loadDataFromDB() {
        // 开始读取数据动画并隐藏数据界面
        viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.contentLayout);

        new Thread(() -> {
            Selector selector = new Selector();
            selector.and("isLocal", "=", true).orderBy("localSaveDateTime", true);
            letterModels = DatabaseUtil.findAll(LetterModel.class, selector);

            Log.d(LocalLetterListActivity.class.getSimpleName(), "读取本地数据有<信件> " + (letterModels == null ? 0 : letterModels.size()) + "  条");

            handler.post(() -> {
                // 停止读取数据动画并显示数据界面
                viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.contentLayout);

                // 本地没有数据
                if (SetUtil.isEmpty(letterModels)) {
                    viewDataBinding.localLetterListView.setVisibility(View.GONE);
                    viewDataBinding.messageLayout.setVisibility(View.VISIBLE);
                }
                // 有本地数据
                else {
                    viewDataBinding.messageLayout.setVisibility(View.GONE);
                    viewDataBinding.localLetterListView.setVisibility(View.VISIBLE);

                    localLetterAdapter.update(letterModels);
                }
            });
        }).start();
    }

}
