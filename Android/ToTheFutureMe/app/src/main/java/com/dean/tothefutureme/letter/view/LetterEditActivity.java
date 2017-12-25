package com.dean.tothefutureme.letter.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.custom.view.edittext.LetterEditText;
import com.dean.tothefutureme.databinding.ActivityLetterEditBinding;
import com.dean.tothefutureme.letter.model.LetterModel;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.timeline.view.TimeLineFragment;
import com.dean.tothefutureme.utils.TokenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 信件编辑Activity
 * <p>
 * Created by dean on 2017/12/8.
 */
@ContentView(R.layout.activity_letter_edit)
public class LetterEditActivity extends ConvenientActivity<ActivityLetterEditBinding> implements Toolbar.OnMenuItemClickListener, LetterEditText.OnMaxLengthListener {

    private ProgressDialog waitDialog;
    private AlertDialog uploadDialog;

    private LetterModel letterModel;

    private boolean isEditModel = false;
    private boolean isLookModel = false;

    private Handler handler = new Handler();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 是否是查看模式
        isLookModel = getIntent().getBooleanExtra(TimeLineFragment.class.getSimpleName(), false);

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        viewDataBinding.toolbar.setTitle(isLookModel ? "信件详细" : "编辑信件");
        setSupportActionBar(viewDataBinding.toolbar);
        viewDataBinding.toolbar.setNavigationOnClickListener(v -> exitLetterEditActivity());
        viewDataBinding.toolbar.setOnMenuItemClickListener(this);

        // 设置信件EditText的高度值给信件EditText
        viewDataBinding.contentEditText.post(() -> {
            // 内容显示控件宽高设置
            viewDataBinding.contentTextView.setHeightAndWidth(viewDataBinding.letterLayout.getHeight(), viewDataBinding.contentTextView.getWidth());
            // 内容编辑控件宽高设置
            viewDataBinding.contentEditText.setHeightAndWidth(viewDataBinding.letterLayout.getHeight(), viewDataBinding.contentTextView.getWidth());
        });

        letterModel = (LetterModel) getIntent().getSerializableExtra(LetterModel.class.getSimpleName());
        if (letterModel == null)
            letterModel = new LetterModel();

        viewDataBinding.setLetterModel(letterModel);
        updateLetterLengthLimit();

        if (isLookModel)
            setReadLetter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isLookModel)
            getMenuInflater().inflate(R.menu.menu_activity_letter_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            // 编辑
            case R.id.menuEditLetter:
                isEditModel = !isEditModel;

                // 更改顶部图标
                item.setIcon(isEditModel ? R.drawable.ic_menu_save : R.drawable.ic_menu_user_info_edit);
                // 切换内容控件显示隐藏
                viewDataBinding.contentTextView.setVisibility(isEditModel ? View.GONE : View.VISIBLE);
                viewDataBinding.contentEditText.setVisibility(isEditModel ? View.VISIBLE : View.GONE);

                // 编辑模式
                if (isEditModel) {
                    // EditText获得焦点
                    viewDataBinding.contentEditText.requestFocus();
                }
                // 非编辑模式
                else if (!isEditModel) {
                    // 收起键盘
                    KeyboardUtil.hideSoftKeyboard(LetterEditActivity.this);
                    // 保存到本地数据库
                    saveLetter();
                }
                break;
            // 上传
            case R.id.menuUploadLetter:
                // 检查内容
                if (letterModel == null || TextUtils.isEmpty(letterModel.getContent())) {
                    ToastUtil.showToast(LetterEditActivity.this, "传送没有内容的信件毫无意义哟！", Toast.LENGTH_LONG, ToastUtil.LOCATION_MIDDLE);
                    return true;
                }
                if (letterModel.getReceiveDateTime() <= 0) {
                    ToastUtil.showToast(LetterEditActivity.this, "时光机也需要知道传送到哪天哟！", Toast.LENGTH_LONG, ToastUtil.LOCATION_MIDDLE);
                    return true;
                }

                // 弹出上传提示
                AlertDialog.Builder builder = new AlertDialog.Builder(LetterEditActivity.this);
                builder.setTitle("确认传送吗？");
                builder.setMessage("传送后，将不能修改，并且只能在指定时期再见到");
                builder.setPositiveButton("确认传送", (dialog, which) -> {
                    uploadDialog.dismiss();
                    uploadLetter();
                });
                builder.setNeutralButton("放弃传送", (dialog, which) -> uploadDialog.dismiss());
                uploadDialog = builder.create();
                uploadDialog.show();
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

    private Date receiveDate;

    /**
     * 选择收件日期
     */
    @OnClick(R.id.receiveDateTextView)
    public void selectReceiveDate() {
        if (!isEditModel || isLookModel)
            return;

        if (letterModel.getReceiveDateTime() <= 0)
            receiveDate = new Date();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {

            receiveDate.setYear(year - 1900);
            receiveDate.setMonth(month);
            receiveDate.setDate(dayOfMonth);

            letterModel.setReceiveDateTime(receiveDate.getTime());
        }, 1900 + receiveDate.getYear(), receiveDate.getMonth(), receiveDate.getDate());
        datePickerDialog.show();
    }

    /**
     * 保存信件
     */
    private void saveLetter() {
        waitDialog = ConvenientProgressDialog.getInstance(this, "正在保存...", false);
        waitDialog.show();

        // 保存到数据库
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
        isEditModel = false;

        // 保存完成后是否提示
        if (tagEnd && waitDialog != null) {
            handler.post(() -> {
                // 发送消息更新广播->更新本地信件列表
                LetterEditActivity.this.sendBroadcast(new Intent(AppConfig.BROADCAST_RECEIVER_DATA_UPDATE));

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
            // 上传到服务器
            upload();
        }).start();
    }

    /**
     * 上传
     */
    private void upload() {
        List<String> urlParams = new ArrayList<>();
        urlParams.add(TTFMApplication.getAuthModel().getToken());

        ConvenientHttpConnection connection = new ConvenientHttpConnection();
        connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.LETTER_UPLOAD, null, urlParams, JSONUtil.object2Json(letterModel).toString(),
                new HttpConnectionListener() {

                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            String code = response.getString("code");
                            // 上传成功
                            handler.post(() -> {
                                if (AppConfig.RESPONSE_SUCCESS.equals(code)) {
                                    ToastUtil.showToast(LetterEditActivity.this, "信件传送成功！");
                                    new Thread(() -> {
                                        // 删除本地信件实例
                                        DatabaseUtil.delete(letterModel);
                                        // 发送消息更新广播->更新本地信件列表
                                        LetterEditActivity.this.sendBroadcast(new Intent(AppConfig.BROADCAST_RECEIVER_DATA_UPDATE));
                                    }).start();

                                    LetterEditActivity.this.finish();
                                } else
                                    onError(-2);
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onError(-1);
                        }
                    }

                    @Override
                    public void onError(int i) {
                        handler.post(() -> {
                            if (waitDialog != null)
                                waitDialog.dismiss();
                            ToastUtil.showToast(LetterEditActivity.this, "上传失败 " + i);
                        });
                    }

                    @Override
                    public void onTokenFailure() {
                        handler.post(() -> {
                            if (waitDialog != null)
                                waitDialog.dismiss();
                            TokenUtils.loginAgain(LetterEditActivity.this);
                        });
                    }

                    @Override
                    public void onEnd() {
                    }
                });
    }

    /**
     * 更新文字长度限制
     */
    private void updateLetterLengthLimit() {
        // 设置文字长度限制
        viewDataBinding.contentEditText.setLetterLengthLimit(letterModel.getLetterLengthLimit());
        // 设置最大文字长度监听器
        viewDataBinding.contentEditText.setOnMaxLengthListener(this);
    }

    /**
     * 退出Activity
     */
    private void exitLetterEditActivity() {
        if (isEditModel) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LetterEditActivity.this);
            builder.setMessage("您的信件还未保存");
            builder.setNegativeButton("保存信件", (dialog, which) -> saveLetter());
            builder.setPositiveButton("放弃并退出", (dialog, which) -> LetterEditActivity.this.finish());
            builder.setCancelable(true);
            builder.create().show();
        } else
            LetterEditActivity.this.finish();
    }

    /**
     * 输入内容达到最大长度
     */
    @Override
    public void onMaxLength() {
        ToastUtil.showToast(this, "达到最大文字长度，执行缴费方法");
    }

    /**
     * 设置信件已读
     */
    private void setReadLetter() {
        new Thread(() -> {
            // 更新数据库已读
            letterModel.setIsRead(1);
            DatabaseUtil.saveOrUpdate(letterModel);

            // 设置服务器指定信件已读
            List<String> urlParams = new ArrayList<>();
            urlParams.add(TTFMApplication.getAuthModel().getToken());
            urlParams.add(letterModel.getLetterId());
            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.LETTER_LOAD_READ, null, urlParams, (String) null,
                    null);
        }).start();

        // 发送信件已读更新广播
        Intent intent = new Intent(AppConfig.BROADCAST_RECEIVER_LETTER_READ_UPDATE);
        intent.putExtra(LetterModel.class.getSimpleName(), letterModel);
        LetterEditActivity.this.sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        if (waitDialog != null && waitDialog.isShowing())
            waitDialog.dismiss();

        super.onDestroy();
    }
}
