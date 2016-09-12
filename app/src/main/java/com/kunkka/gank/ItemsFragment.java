package com.kunkka.gank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kunkka.gank.pojo.Item;

import java.util.List;
import java.util.Map;

public class ItemsFragment extends Fragment implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private MainAdapter mMainAdapter;
    private RecyclerView rv;
    private LinearLayoutManager mLayoutManager;

    public ItemsFragment() {
    }

    public static ItemsFragment newInstance(MainContract.Presenter presenter) {
        ItemsFragment fragment = new ItemsFragment();
        fragment.mPresenter = presenter;
        Bundle args = new Bundle();
//        args.put("", presenter);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.content_main, container, false);
        rv = (RecyclerView) root.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        mMainAdapter = new MainAdapter(getContext());
        rv.setAdapter(mMainAdapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mPresenter != null && dy > 0) {
                    int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition + 1 == mMainAdapter.getItemCount()) {
                        mPresenter.loadMore();
                    }
                }
            }
        });
        return root;
    }

    @Override
    public void addItems(Map<String, List<Item>> items) {
        mMainAdapter.setItems(items);
    }

    @Override
    public void showError() {

    }
}
