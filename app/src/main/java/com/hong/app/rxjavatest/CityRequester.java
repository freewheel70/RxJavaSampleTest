package com.hong.app.rxjavatest;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/4/12.
 */
public class CityRequester {

    public static void loadCityList(final Subscriber subscriber) {

        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> stringList = NetworkHelper.requestCityList();
                subscriber.onNext(stringList);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        Observable.from(strings)
                                .flatMap(new Func1<String, Observable<String>>() {
                                    @Override
                                    public Observable<String> call(final String s) {

                                        return Observable.create(new Observable.OnSubscribe<String>() {
                                            @Override
                                            public void call(Subscriber<? super String> subscriber) {
                                                subscriber.onNext("City: " + s);
                                            }
                                        });
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(subscriber);
                    }
                });
    }
}
