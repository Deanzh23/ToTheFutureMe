package com.dean.tothefutureme.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 百分比的相对布局
 * <p>
 * Created by dean on 2018/1/4.
 */
public class PercentageRelativeLayout extends RelativeLayout {

    private int layoutWidth;

    public PercentageRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childViewCount = getChildCount();

        for (int i = 0; i < childViewCount; i++) {
            View childView = getChildAt(i);

            if (childView.getTag() != null) {
                Log.d(PercentageRelativeLayout.class.getSimpleName(), "childView.getTag() == " + childView.getTag());

                resetViewLocation(childView);
            }

        }

        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 重新设置子View的位置
     *
     * @param view
     */
    private void resetViewLocation(View view) {
        view.post(() -> {
            Log.d(PercentageRelativeLayout.class.getSimpleName(), "[resetViewLocation] --> view'width == " + view.getWidth());

            String tag = (String) view.getTag();
            float location = layoutWidth / 10f * Float.valueOf(tag);
            view.setX(location - view.getWidth() / 2);
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutWidth = measureWidth(widthMeasureSpec);
        Log.d(PercentageRelativeLayout.class.getSimpleName(), "layoutWidth == " + layoutWidth);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

}
