package com.hong.app.freegank.utils;

import android.util.TypedValue;

import com.hong.app.freegank.FreeGankApplication;

/**
 * Created by Freewheel on 2016/5/8.
 */
public class UnitUtil {
    public static int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, FreeGankApplication.getInstance().getResources().getDisplayMetrics());
    }
}
