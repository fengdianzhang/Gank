package com.kunkka.gank;

import android.support.v7.widget.Toolbar;
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
    private final Toolbar mToolbar;

    public MainPresenter(Toolbar toolbar) {
        mToolbar = toolbar;
    }

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
    public void setTitle(String title) {
        if (title != null) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public void loadMore() {
        final String date = mDays.get(mCurrIndex++);
        DailyModel.getInstance().requestDailyData(date, new Subscriber<List<Item>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(List<Item> items) {
                Log.d(TAG, "onNext: " + items);
                mView.addItems(items);
            }
        });
    }
}
