package com.kunkka.gank.model;

import android.util.Log;

import com.kunkka.gank.pojo.Item;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by kunkka on 16/7/31.
 */

public class DailyModel {
    private static final String TAG = "DailyModel";
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

    public void requestDailyData(final String dateStr, Subscriber<List<Item>> subscriber) {
        final Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA).parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDailyService.getDaily(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
                .map(new HttpResultFunc<Map<String, List<Item>>>())
                .map(new Func1<Map<String, List<Item>>, List<Item>>() {
                    @Override
                    public List<Item> call(Map<String, List<Item>> map) {
                        List<Item> list = new ArrayList<>();
                        for (List<Item> items : map.values()) {
                            for (Item item : items) {
                                item.setDate(dateStr);
                                list.add(item);
                            }
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("M月d日 EEEE", Locale.CHINA);
                        Item item = new Item();
                        item.setType("date");
                        item.setTitle(sdf.format(calendar.getTime()));
                        item.setDate(dateStr);
                        list.add(item);
                        return list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private String getDateString(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");
        if (month < 10) {
            sb.append(0);
        }
        sb.append(month).append("-");
        if (day < 10) {
            sb.append(0);
        }
        sb.append(day);
        return sb.toString();
    }
}
