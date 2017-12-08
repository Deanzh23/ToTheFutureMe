package com.dean.tothefutureme.letter.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.database.Selector;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setTitle("本地信件列表");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setOnMenuItemClickListener(this);

        localLetterAdapter = new LocalLetterAdapter(this, letterModels);
        viewDataBinding.localLetterListView.setAdapter(localLetterAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDataFromDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO 跳转信件编写界面
        return true;
    }

    /**
     * 从本地数据库中读取信件数据
     */
    private void loadDataFromDB() {
        // 开始读取数据动画
        viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.contentLayout);

        new Thread(() -> {
            Selector selector = new Selector();
            selector.and("isLocal", "=", true);
            letterModels = DatabaseUtil.findAll(LetterModel.class, selector);

            handler.post(() -> {
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
