package com.kunkka.gank;

import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.widget.CompoundButton;

import com.kunkka.gank.pojo.Item;
import com.kunkka.gank.pojo.Type;

import java.util.Collection;
import java.util.Set;

/**
 * Created by kunkka on 16/7/31.
 */

public interface MainContract {
    interface Presenter {
        void setMainView(MainView mainView);

        void setItemView(ItemView itemView);

        DrawerListener getDrawerListener();

        void start();

        void setTitle(String title);

        void loadMore();
    }

    interface MainView {
        void setTitle(String title);

        void initMenu(Collection<Type> types, CompoundButton.OnCheckedChangeListener listener);
    }

    interface ItemView {
        void addItems(Collection<Item> items);

        void filter(Set<String> filterNames);

        void showError();
    }
}
