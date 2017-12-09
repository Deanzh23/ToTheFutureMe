package com.dean.tothefutureme.letter.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
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

    private ProgressDialog waitDialog;
    private AlertDialog deleteDialog;

    private List<LetterModel> letterModels;

    private Handler handler = new Handler();

    /**
     * 数据更新监听器
     */
    public interface OnDataChangeUpdateListener {
        void onDataChanged();
    }

    private OnDataChangeUpdateListener onDataChangeUpdateListener;

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
        LetterModel letterModel = letterModels.get(i);
        adapterLocalLetterBinding.setLetterModel(letterModel);

        // 携带信件Model跳转到编辑
        adapterLocalLetterBinding.letterContentLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, LetterEditActivity.class);
            intent.putExtra(LetterModel.class.getSimpleName(), letterModel);
            context.startActivity(intent);
        });

        // 长按删除
        adapterLocalLetterBinding.letterContentLayout.setOnLongClickListener(v -> {
            showDeleteDialog(letterModel);
            return true;
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

    /**
     * 更新列表数据
     *
     * @param letterModels
     */
    public void update(List<LetterModel> letterModels) {
        this.letterModels = letterModels;

        notifyDataSetChanged();
    }

    /**
     * 提示用户是否确认删除信件
     *
     * @param letterModel
     */
    private void showDeleteDialog(LetterModel letterModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要删除信件吗");
        builder.setNeutralButton("确认删除", (dialog, which) -> deleteLetter(letterModel));
        builder.setPositiveButton("取消", (dialog, which) -> {
            if (deleteDialog != null)
                deleteDialog.dismiss();
        });
        deleteDialog = builder.create();
        deleteDialog.show();
    }

    /**
     * 删除信件Model
     *
     * @param letterModel
     */
    private void deleteLetter(LetterModel letterModel) {
        waitDialog = ConvenientProgressDialog.getInstance(context, "正在删除...", false);
        waitDialog.show();

        // 从数据库中删除信件Model
        new Thread(() -> {
            DatabaseUtil.delete(letterModel);
            handler.post(() -> {
                waitDialog.dismiss();
                ToastUtil.showToast(context, "删除成功");
                if (onDataChangeUpdateListener != null)
                    onDataChangeUpdateListener.onDataChanged();
            });
        }).start();
    }

    public void setOnDataChangeUpdateListener(OnDataChangeUpdateListener onDataChangeUpdateListener) {
        this.onDataChangeUpdateListener = onDataChangeUpdateListener;
    }
}
