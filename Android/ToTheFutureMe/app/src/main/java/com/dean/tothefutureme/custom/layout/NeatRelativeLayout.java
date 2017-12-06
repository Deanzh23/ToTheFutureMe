package com.dean.tothefutureme.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dean.android.framework.convenient.util.SetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 左右整齐Layout
 * <p>
 * Created by dean on 2017/12/6.
 */
public class NeatRelativeLayout extends RelativeLayout {

    private Context context;

    /**
     * 调整的View集合
     */
    private List<View> adjustmentViews;
    /**
     * 是否经过了调整
     */
    private boolean isAdjustmented = false;

    public NeatRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(NeatRelativeLayout.class.getSimpleName(), "[onLayout]");
        super.onLayout(changed, l, t, r, b);

        if (isAdjustmented)
            return;

        adjustmentViews = new ArrayList<>();

        int childCount = getChildCount();

        Log.d(NeatRelativeLayout.class.getSimpleName(), "childCount = " + childCount);

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            if (!(childView instanceof ViewGroup))
                continue;

            View view = getAdjustmentView((ViewGroup) getChildAt(i));
            if (view != null)
                adjustmentViews.add(view);
        }

        adjustmentViewWidth();
    }

    /**
     * 获取需要调整的View对象
     *
     * @param layout
     * @return
     */
    private View getAdjustmentView(ViewGroup layout) {
        View adjustmentView = null;

        try {
            adjustmentView = layout.getChildAt(0);
        } catch (Exception e) {
        }

        return adjustmentView;
    }

    /**
     * 调整View集合中view的宽度
     */
    private void adjustmentViewWidth() {
        isAdjustmented = true;

        Log.d(NeatRelativeLayout.class.getSimpleName(), "[adjustmentViewWidth]");

        if (SetUtil.isEmpty(adjustmentViews))
            return;

        int maxWidth = -1;

        // 获得最小宽度值
        int size = adjustmentViews == null ? 0 : adjustmentViews.size();
        for (int i = 0; i < size; i++) {
            View view = adjustmentViews.get(i);
            int tempWidth = view.getWidth();

            maxWidth = (maxWidth == -1 || maxWidth < tempWidth) ? tempWidth : maxWidth;
        }

        if (maxWidth < 0)
            return;

        // 重新设置控件宽度为前面的最小宽度值
        for (int i = 0; i < size; i++) {
            View view = adjustmentViews.get(i);
            int tempWidth = view.getWidth();

            if (maxWidth > tempWidth) {
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                layoutParams.width = maxWidth;
                view.setLayoutParams(layoutParams);
            }
        }
    }

}
