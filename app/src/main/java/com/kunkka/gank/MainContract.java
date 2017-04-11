package com.kunkka.gank;

import com.kunkka.gank.pojo.Item;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by kunkka on 16/7/31.
 */

public interface MainContract {
    interface Presenter {
        void setView(View view);

        void start();

        void setTitle(String title);

        void loadMore();
    }

    interface View {
        void addItems(Collection<Item> items);

        void showError();
    }
}
