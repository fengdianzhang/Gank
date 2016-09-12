package com.kunkka.gank;

import android.util.Log;

import com.kunkka.gank.model.DailyModel;
import com.kunkka.gank.pojo.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import rx.Subscriber;

/**
 * Created by kunkka on 16/7/31.
 */

public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = "MainPresenter";

    private MainContract.View mView;
    private final List<String> mDays = new ArrayList<>();
    private int mCurrIndex = 0;

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        DailyModel.getInstance().requestHistoryDays(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {
                loadMore();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "get days failed " + e.getMessage());
            }

            @Override
            public void onNext(List<String> strings) {
                mDays.addAll(strings);
            }
        });
    }

    @Override
    public void loadMore() {
        final String date = mDays.get(mCurrIndex++);
        String[] yymmdd = date.split("-");
        int year = Integer.parseInt(yymmdd[0]);
        int month = Integer.parseInt(yymmdd[1]);
        int day = Integer.parseInt(yymmdd[2]);
        DailyModel.getInstance().requestDailyData(year, month, day, new Subscriber<Map<String, List<Item>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "request complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "request error, msg = " + e.getMessage());
            }

            @Override
            public void onNext(Map<String, List<Item>> map) {
                Log.d(TAG, "request success, Map = " + map);
//                mMainAdapter.setItems(stringItemMap);
                Item item = new Item();
                item.setType("date");
                item.setTitle(date);
                List<Item> list = new ArrayList<Item>();
                list.add(item);
                map.put("date", list);
                mView.addItems(map);
            }
        });
    }
}
