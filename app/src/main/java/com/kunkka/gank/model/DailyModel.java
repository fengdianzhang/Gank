package com.kunkka.gank.model;

import com.kunkka.gank.pojo.Item;

import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kunkka on 16/7/31.
 */

public class DailyModel {
    private static final String DOMAIN = "http://gank.io/api/";
    private static final DailyModel INSTANCE = new DailyModel();

    private final Retrofit mRetrofit;
    private final DailyService mDailyService;

    public static DailyModel getInstance() {
        return INSTANCE;
    }

    private DailyModel() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDailyService = mRetrofit.create(DailyService.class);
    }

    public void requestHistoryDays(Subscriber<List<String>> subscriber) {
        mDailyService.getHistoryDays()
                .map(new HttpResultFunc<List<String>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void requestDailyData(int year, int month, int day, Subscriber<Map<String, List<Item>>> subscriber) {
        mDailyService.getDaily(year, month, day)
                .map(new HttpResultFunc<Map<String, List<Item>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
