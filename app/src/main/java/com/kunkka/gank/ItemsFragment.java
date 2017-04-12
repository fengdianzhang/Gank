package com.kunkka.gank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kunkka.gank.MainContract.ItemView;
import com.kunkka.gank.pojo.Item;

import java.util.Collection;
import java.util.Set;

public class ItemsFragment extends Fragment implements ItemView {
    private static final String TAG = "ItemsFragment";
    private MainContract.Presenter mPresenter;
    private MainAdapter mMainAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public static ItemsFragment newInstance(MainContract.Presenter presenter) {
        ItemsFragment fragment = new ItemsFragment();
        fragment.mPresenter = presenter;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.content_main, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMainAdapter = new MainAdapter(getContext());
        mRecyclerView.setAdapter(mMainAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mPresenter != null) {
                    int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition + 1 == mMainAdapter.getItemCount()) {
                        mPresenter.loadMore();
                    }
                    mPresenter.setTitle(mMainAdapter.getCurrTitle(firstVisiblePosition));
                }
            }
        });
        return root;
    }

    @Override
    public void addItems(Collection<Item> items) {
        mMainAdapter.addItems(items);
    }

    @Override
    public void filter(Set<String> filterNames) {
        if (mMainAdapter.filter(filterNames)) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void showError() {

    }
}
