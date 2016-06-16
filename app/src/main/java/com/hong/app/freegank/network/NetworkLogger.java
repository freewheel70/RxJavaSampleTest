package com.hong.app.freegank.network;


import com.hong.app.freegank.BuildConfig;

/**
 * Created by ftuser on 23/3/16.
 */
public class NetworkLogger {

    private NetworkLogger() {
    }

    public static void printMessageIfDebug(String msg) {
        if (BuildConfig.DEBUG) {
            System.out.println(msg);
        }
    }

}
