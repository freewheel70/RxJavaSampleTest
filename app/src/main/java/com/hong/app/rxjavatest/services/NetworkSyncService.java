package com.hong.app.rxjavatest.services;

import android.app.IntentService;
import android.content.Intent;

import com.hong.app.rxjavatest.Events.ServerSyncBlogEvent;
import com.hong.app.rxjavatest.database.User;
import com.hong.app.rxjavatest.network.BlogNetworkManager;
import com.hong.app.rxjavatest.network.NetworkResponseResult;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/22.
 */
public class NetworkSyncService extends IntentService {

    private static final String TAG = "NetworkSyncService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NetworkSyncService(String name) {
        super(name);
    }

    public NetworkSyncService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        uploadNonSyncedBlogs();
    }

    private void downloadAllBlogs() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                if (User.getUser().isAnonymous()) {
                    subscriber.onError(new Throwable("登录账号以同步云服务"));
                } else {
                    NetworkResponseResult responseResult = BlogNetworkManager.getMyBlogs();
                    if (responseResult.success) {
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Throwable(responseResult.message));
                    }

                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Object>() {

                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new ServerSyncBlogEvent("", true));
                    }


                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new ServerSyncBlogEvent("下载错误 " + e.getMessage(), false));
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    private void uploadNonSyncedBlogs() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                if (User.getUser().isAnonymous()) {
                    subscriber.onError(new Throwable("登录账号以同步云服务"));
                } else {
                    NetworkResponseResult responseResult = BlogNetworkManager.uploadNonSyncedBlogs();
                    if (responseResult.success) {
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Throwable(responseResult.message));
                    }
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        downloadAllBlogs();
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new ServerSyncBlogEvent("上传错误 " + e.getMessage(), false));
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

}
