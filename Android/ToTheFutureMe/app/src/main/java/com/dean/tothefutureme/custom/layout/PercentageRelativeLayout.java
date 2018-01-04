package com.dean.tothefutureme.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.dean.android.framework.convenient.screen.ScreenUtils;

/**
 * 百分比的相对布局
 * <p>
 * Created by dean on 2018/1/4.
 */
public class PercentageRelativeLayout extends RelativeLayout {

    private Context context;
    private int layoutWidth;

    public PercentageRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int childViewCount = getChildCount();
        int transverseCheap = 0;

        for (int i = 0; i < childViewCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                if (childView.getTag() != null) {
                    int childViewWidth = childView.getMeasuredWidth();
                    transverseCheap = (int) (layoutWidth / 10f * 3 - l - (childViewWidth / 2) - ScreenUtils.dp2px(context, 2));
                }

                l += transverseCheap;
                childView.layout(l, 0, r, b);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutWidth = measureWidth(widthMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
        int result = 500;
        //相当于我们设置为wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        }
        //相当于我们设置为match_parent或者为一个具体的值
        else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }

        return result;
    }

}
