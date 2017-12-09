package com.dean.tothefutureme.letter.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.custom.view.edittext.LetterEditText;
import com.dean.tothefutureme.databinding.ActivityLetterEditBinding;
import com.dean.tothefutureme.letter.model.LetterModel;
import com.dean.tothefutureme.main.TTFMApplication;

import java.util.UUID;

/**
 * 信件编辑Activity
 * <p>
 * Created by dean on 2017/12/8.
 */
@ContentView(R.layout.activity_letter_edit)
public class LetterEditActivity extends ConvenientActivity<ActivityLetterEditBinding> implements Toolbar.OnMenuItemClickListener {

    private ProgressDialog waitDialog;

    private LetterModel letterModel;

    private boolean isEditing;

    private Handler handler = new Handler();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle("信件编辑");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> exitLetterEditActivity());
        viewDataBinding.toolbar.setOnMenuItemClickListener(this);

        // 设置信件EditText的高度值给信件EditText
        viewDataBinding.contentEditText.post(() -> {
            Log.d(LetterEditText.class.getSimpleName(), "[ post ]");
            viewDataBinding.contentEditText.setHeightAndWidth(viewDataBinding.scrollView.getHeight(), viewDataBinding.contentEditText.getWidth());
        });

        letterModel = (LetterModel) getIntent().getSerializableExtra(LetterModel.class.getSimpleName());
        if (letterModel == null)
            letterModel = new LetterModel();

        /**
         * debug
         */
        test();

        viewDataBinding.setLetterModel(letterModel);
    }

    private void test() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 100; i++)
            builder.append("刷卡来得及发阿拉山口的卷发； 啊上了打开肌肤啊上了；都快放假啊上来的反馈就仨离开对方离开撒娇地方阿斯利康的风景卡洛斯地方将啊上来的风景看啊上来的反馈就啊上了");

        letterModel.setContent(builder.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_letter_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        viewDataBinding.contentEditText.setEnabled(false);
        isEditing = false;

        switch (item.getItemId()) {
            // 编辑
            case R.id.menuEditLetter:
                viewDataBinding.contentEditText.setEnabled(true);
                isEditing = true;
                break;
            // 保存
            case R.id.menuSaveLetter:
                saveLetter();
                break;
            // 上传
            case R.id.menuUploadLetter:
                uploadLetter();
                break;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitLetterEditActivity();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存信件
     */
    private void saveLetter() {
        waitDialog = ConvenientProgressDialog.getInstance(this, "正在保存...", false);
        waitDialog.show();

        new Thread(() -> save2DB(true)).start();
    }

    /**
     * 保存数据到本地DB
     */
    private void save2DB(boolean tagEnd) {
        // 分配信件ID
        letterModel.setLetterId(TextUtils.isEmpty(letterModel.getLetterId()) ? UUID.randomUUID().toString() : letterModel.getLetterId());
        // 标示是本地信件
        letterModel.setLocal(true);
        // 信件保存的日期时间
        letterModel.setLocalSaveDateTime(System.currentTimeMillis());
        // 发件人ID -> 自己的username
        letterModel.setSenderUserId(TTFMApplication.getAuthModel().getUsername());
        // 收件人ID -> username
        letterModel.setUserId(TTFMApplication.getAuthModel().getUsername());
        // 发件日期时间毫秒值
        letterModel.setSendDateTime(System.currentTimeMillis());

        // 保存到数据库
        DatabaseUtil.saveOrUpdate(letterModel);
        isEditing = false;

        // 保存完成后是否提示
        if (tagEnd && waitDialog != null) {
            handler.post(() -> {
                waitDialog.dismiss();
                ToastUtil.showToast(LetterEditActivity.this, "保存成功");
            });
        }
    }

    /**
     * 上传信件到服务器
     */
    private void uploadLetter() {
        waitDialog = ConvenientProgressDialog.getInstance(this, "正在上传...", false);
        waitDialog.show();

        new Thread(() -> {
            // 先在本地保存一下，以防上传途中意外丢失内存中的数据
            save2DB(false);
            // TODO 上传到服务器
        }).start();
    }

    /**
     * 退出Activity
     */
    private void exitLetterEditActivity() {
        if (isEditing) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LetterEditActivity.this);
            builder.setMessage("您的信件还未保存");
            builder.setNegativeButton("保存信件", (dialog, which) -> new Thread(() -> saveLetter()).start());
            builder.setPositiveButton("放弃并退出", (dialog, which) -> LetterEditActivity.this.finish());
            builder.setCancelable(true);
            builder.create().show();
        } else
            LetterEditActivity.this.finish();
    }

}
