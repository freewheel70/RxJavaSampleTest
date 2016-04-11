package com.hong.app.rxjavatest;

import android.graphics.Bitmap;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ImageLoader {

    private static ImageLoader imageLoader;
    private String imageUrl;

    Observable<Bitmap> imageObservable;

    private ImageLoader() {
        imageObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = NetworkHelper.getBitmapfromUrl(imageUrl);
                subscriber.onNext(bitmap);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static ImageLoader getInstance() {
        if (imageLoader == null) {
            synchronized (ImageLoader.class) {
                imageLoader = new ImageLoader();
            }
        }

        return imageLoader;
    }

    public void loadImage(String imageUrl, Subscriber<Bitmap> subscriber) {
        this.imageUrl = imageUrl;
        imageObservable.subscribe(subscriber);
    }

}
