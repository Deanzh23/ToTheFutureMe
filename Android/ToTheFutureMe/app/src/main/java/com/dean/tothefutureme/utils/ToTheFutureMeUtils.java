package com.dean.tothefutureme.utils;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * App工具类
 * <p>
 * Created by dean on 2017/12/24.
 */
public class ToTheFutureMeUtils {

    @BindingAdapter({"bindText_GenderCode2String"})
    public static void bindGenderCode2String(TextView textView, int genderCode) {
        if (genderCode > 1)
            return;

        switch (genderCode) {
            case 0:
                textView.setText("女");
                break;
            case 1:
                textView.setText("男");
                break;
        }
    }

}
