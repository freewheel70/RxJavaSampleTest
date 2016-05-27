package com.hong.app.rxjavatest;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.hong.app.rxjavatest.Utils.FileUtil;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;

/**
 * Created by Administrator on 2016/4/18.
 */
public class FreeGankApplication extends Application {

    private static final String TAG = "FreeGankApplication";

    private static FreeGankApplication application;

    public static FreeGankApplication getInstance() {
        return application;
    }

    private int activityNum = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        application = this;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                activityNum++;
                if (activityNum == 1) {
                    Log.d(TAG, "enter foreground");
                }
                Log.d(TAG, "onActivityStarted: activityNum " + activityNum);
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityNum--;

                if (activityNum == 0) {
                    Log.d(TAG, "enter background");
                }
                Log.d(TAG, "onActivityStopped: activityNum " + activityNum);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        createDirIfNeed();
    }

    private void createDirIfNeed() {
        File dir = new File(FileUtil.PRIVATE_IMAGE_STORAGE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
