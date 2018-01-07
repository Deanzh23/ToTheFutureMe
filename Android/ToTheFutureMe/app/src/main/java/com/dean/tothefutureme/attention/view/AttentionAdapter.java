package com.dean.tothefutureme.attention.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;
import com.dean.android.framework.convenient.bitmap.util.BitmapUtil;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.fw.convenient.ui.view.loading.progress.ConvenientProgressDialog;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.attention.model.AttentionModel;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.databinding.AdapterAttentionBinding;
import com.dean.tothefutureme.main.TTFMApplication;
import com.dean.tothefutureme.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友Adapter
 * <p>
 * Created by dean on 2018/1/5.
 */
public class AttentionAdapter extends ConvenientAdapter<AdapterAttentionBinding> {

    private Activity activity;
    private List<AttentionModel> attentionModels;

    private ProgressDialog waitDialog;
    private AlertDialog deleteDialog;

    private OnDeleteAttentionListener onDeleteAttentionListener;

    public interface OnDeleteAttentionListener {

        void onDelete();
    }

    public AttentionAdapter(Activity activity, List<AttentionModel> attentionModels, OnDeleteAttentionListener onDeleteAttentionListener) {
        this.activity = activity;
        this.attentionModels = attentionModels;
        this.onDeleteAttentionListener = onDeleteAttentionListener;
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

        // 长按解除关注关系
        adapterAttentionBinding.getRoot().setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("解除与" + attentionModel.getNickname() + "的关注吗？");
            builder.setPositiveButton("解除关注", (dialog, which) -> {
                waitDialog = ConvenientProgressDialog.getInstance(activity, "正在解除，请稍后...", false);
                waitDialog.show();

                deleteAttention(attentionModel);
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
                if (deleteDialog != null)
                    deleteDialog.dismiss();
            });
            deleteDialog = builder.create();
            deleteDialog.show();

            return true;
        });
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

    /**
     * 更新关注数据
     *
     * @param attentionModels
     */
    public void update(List<AttentionModel> attentionModels) {
        this.attentionModels = attentionModels;
        notifyDataSetChanged();
    }

    /**
     * 解除关注关系
     *
     * @param attentionModel
     */
    private void deleteAttention(AttentionModel attentionModel) {
        new Thread(() -> {
            List<String> urlParams = new ArrayList<>();
            urlParams.add(TTFMApplication.getAuthModel().getToken());

            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(AppConfig.BASE_URL + AppConfig.ATTENTION_DELETE_ATTENTION, null, urlParams,
                    JSONUtil.object2Json(attentionModel).toString(), new OnHttpConnectionListener() {
                        @Override
                        public void onSuccess(String s) {
                            activity.runOnUiThread(() -> {
                                if (onDeleteAttentionListener != null)
                                    onDeleteAttentionListener.onDelete();
                            });
                        }

                        @Override
                        public void onError(int i) {
                            activity.runOnUiThread(() -> ToastUtil.showToast(activity, "解除关注失败 " + i));
                        }

                        @Override
                        public void onTokenFailure() {
                            activity.runOnUiThread(() -> TokenUtils.loginAgain(activity));
                        }

                        @Override
                        public void onEnd() {
                            activity.runOnUiThread(() -> {
                                if (waitDialog != null)
                                    waitDialog.dismiss();
                            });
                        }
                    });

        }).start();
    }

}
