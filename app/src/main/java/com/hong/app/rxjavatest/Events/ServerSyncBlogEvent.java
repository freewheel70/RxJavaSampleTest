package com.hong.app.rxjavatest.Events;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ServerSyncBlogEvent {

    public boolean success;

    public String message;

    public ServerSyncBlogEvent(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
