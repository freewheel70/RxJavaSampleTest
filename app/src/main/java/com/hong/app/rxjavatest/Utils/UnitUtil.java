package com.hong.app.rxjavatest.Utils;

import android.util.TypedValue;

import com.hong.app.rxjavatest.FreeGankApplication;

/**
 * Created by Administrator on 2016/5/8.
 */
public class UnitUtil {
    public static int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, FreeGankApplication.getInstance().getResources().getDisplayMetrics());
    }
}
