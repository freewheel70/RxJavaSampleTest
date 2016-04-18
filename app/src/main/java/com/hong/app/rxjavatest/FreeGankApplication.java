package com.hong.app.rxjavatest;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Administrator on 2016/4/18.
 */
public class FreeGankApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
