package com.kunkka.gank;

import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.kunkka.gank.MainContract.ItemView;
import com.kunkka.gank.MainContract.MainView;
import com.kunkka.gank.MainContract.Presenter;
import com.kunkka.gank.model.DailyModel;
import com.kunkka.gank.pojo.Item;
import com.kunkka.gank.pojo.Type;
import com.kunkka.gank.tools.PrefUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Subscriber;

/**
 * Created by kunkka on 16/7/31.
 */

public class MainPresenter implements Presenter, OnCheckedChangeListener {
    private static final String TAG = "MainPresenter";
    private static final String FILTER_TYPE_NAME_KEY = "filter_type_name";
    private static final String SEPARATOR = "-";

    private ItemView mItemView;
    private MainView mMainView;

    private final List<String> mDays = new ArrayList<>();
    private int mCurrIndex = 0;
    private final Set<String> mFilterNames = new HashSet<>();
    private boolean mFilterModified = false;
    private DrawerListener mDrawerListener = new SimpleDrawerListener() {
        @Override
        public void onDrawerClosed(View drawerView) {
            Log.d(TAG, "onDrawerClosed");
            if (mFilterModified) {
                mItemView.filter(mFilterNames);
            }
        }
    };

    @Override
    public void setMainView(MainView mainView) {
        mMainView = mainView;
        Map<String, Type> typeMap = Type.getTypeMap();
        mFilterNames.addAll(getFilterType());
        for (String name : mFilterNames) {
            Type type = typeMap.get(name);
            if (type != null) {
                type.setEnable(false);
            }
        }
        mainView.initMenu(typeMap.values(), this);
    }

    @Override
    public void setItemView(ItemView itemView) {
        mItemView = itemView;
    }

    @Override
    public DrawerListener getDrawerListener() {
        return mDrawerListener;
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
        if (title != null && mMainView != null) {
            mMainView.setTitle(title);
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
                mItemView.addItems(items);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String type = (String) buttonView.getTag();
        boolean modified = false;
        if (isChecked) {
            modified = mFilterNames.remove(type);
        } else {
            modified = mFilterNames.add(type);
        }
        if (modified) {
            mFilterModified = true;
            saveFilterType();
        }
    }

    private List<String> getFilterType() {
        String saved = PrefUtil.getFromPrefs(GankApplication.getApplication(), FILTER_TYPE_NAME_KEY, "");
        if (TextUtils.isEmpty(saved)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(saved.split(SEPARATOR));
        }
    }

    private void saveFilterType() {
        StringBuilder sb = new StringBuilder();
        for (String name : mFilterNames) {
            sb.append(SEPARATOR).append(name);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        PrefUtil.saveToPrefs(GankApplication.getApplication(), FILTER_TYPE_NAME_KEY, sb.toString());
    }
}
