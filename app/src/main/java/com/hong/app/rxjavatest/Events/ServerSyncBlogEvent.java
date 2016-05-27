package com.hong.app.rxjavatest.Events;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ServerSyncBlogEvent {

    private boolean success;

    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public ServerSyncBlogEvent(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
