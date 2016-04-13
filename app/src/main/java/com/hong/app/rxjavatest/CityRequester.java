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

        Observable
                .create(new Observable.OnSubscribe<List<String>>() {
                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        List<String> stringList = NetworkHelper.requestCityList();
                        subscriber.onNext(stringList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {

                        return Observable.from(strings);
                    }
                })
                .map(new Func1<String, String>() {

                    @Override
                    public String call(String s) {
                        return "City: " + s;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        
    }
}
