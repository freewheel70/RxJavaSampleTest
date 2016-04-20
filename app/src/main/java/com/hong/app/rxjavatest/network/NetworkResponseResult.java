package com.hong.app.rxjavatest.network;

/**
 * Created by Administrator on 2016/4/20.
 */
public class NetworkResponseResult {

    public boolean success;

    public String message;

    public NetworkResponseResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
