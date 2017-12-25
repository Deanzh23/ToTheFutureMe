package com.dean.tothefutureme.custom.view.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.dean.tothefutureme.custom.view.edittext.LetterEditText;

/**
 * 信件（每行都带有行线的）TextView
 * <p>
 * 1.android:background="@android:color/transparent"
 * 2.android:lineSpacingExtra="6dp"
 * 3.android:scrollbars="vertical"
 * </p>
 * Created by dean on 2017/12/9.
 */
@SuppressLint("AppCompatCustomView")
public class LetterTextView extends TextView {

    private Paint linePaint;

    /**
     * 信件EditText的高度值
     */
    private int height;
    /**
     * 信件EditText的宽度值
     */
    private int width;

    public LetterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        // 设置TextView可以滚动
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    /**
     * 设置线画笔
     *
     * @param linePaint
     */
    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
        postInvalidate();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        // 线 画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.GRAY);
        linePaint.setStyle(Paint.Style.FILL);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        // 行高度
        int lineHeight = getLineHeight();
        // 共有多少行
        // 当前文本行数
        int contentLineCount = getLineCount() + 1;
        Log.d(LetterEditText.class.getSimpleName(), "contentLineCount == " + contentLineCount);
        // 屏幕能显示的行数
        int screenLineCount = height / lineHeight;
        Log.d(LetterEditText.class.getSimpleName(), "screenLineCount == " + screenLineCount);
        // 真实的行数
        int lineCount = contentLineCount > screenLineCount ? contentLineCount : screenLineCount;
        Log.d(LetterEditText.class.getSimpleName(), "lineCount == " + lineCount);
        // 画行线
        drawLines(canvas, lineHeight, lineCount);
        // 保存画板
        canvas.save();

        super.onDraw(canvas);
    }

    /**
     * 画 行线
     *
     * @param canvas
     * @param lineHeight
     * @param lineCount
     */
    private void drawLines(Canvas canvas, int lineHeight, int lineCount) {
        for (int i = 0; i < lineCount; i++) {
            float y = (i + 1) * lineHeight;
            canvas.drawLine(0, y, width, y, linePaint);
        }
    }

    /**
     * 设置高度
     *
     * @param height
     */
    public void setHeightAndWidth(int height, int width) {
        this.height = height;
        this.width = width;

        postInvalidate();
    }

}
