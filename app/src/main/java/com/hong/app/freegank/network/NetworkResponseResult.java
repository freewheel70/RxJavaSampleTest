package com.hong.app.freegank.network;

/**
 * Created by Freewheel on 2016/4/20.
 */
public class NetworkResponseResult {

    private boolean success;

    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public NetworkResponseResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
