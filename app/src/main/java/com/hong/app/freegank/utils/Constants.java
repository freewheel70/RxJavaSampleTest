package com.hong.app.freegank.utils;

import android.os.Environment;

import com.hong.app.freegank.FreeGankApplication;

/**
 * Created by Freewheel on 2016/4/24.
 */
public class Constants {

    public static final String FREEGANK_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FreeGank";
    public static final String AVATAR_PATH = FreeGankApplication.getInstance().getFilesDir() + "/freegankAvatar.jpeg";

}
