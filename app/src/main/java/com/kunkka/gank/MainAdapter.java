package com.kunkka.gank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kunkka.gank.pojo.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kunkka on 16/7/26.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mInflater;
    private final List<Item> mItems = new ArrayList<>();
    private final Context mContext;
    private final Comparator<Item> mComparator = new ItemComparator();

    public MainAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setItems(Map<String, List<Item>> items) {
        List<Item> tmp = new ArrayList<>();
        for (List<Item> list : items.values()) {
            tmp.addAll(list);
        }
        Collections.sort(tmp, mComparator);
        mItems.addAll(tmp);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mItems.get(position).getType()) {
            case "date":
                return 1;
            case "福利":
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view = mInflater.inflate(R.layout.main_date_item, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            case 2:
                View view1 = mInflater.inflate(R.layout.main_image_item, parent, false);
                return new ImageViewHolder(view1) {
                };
            case 0:
            default:
                View view2 = mInflater.inflate(R.layout.main_list_item, parent, false);
                return new ItemViewHolder(view2);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = mItems.get(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder ivh = (ItemViewHolder) holder;
            ivh.title.setText(item.getTitle());
            ivh.type.setText(item.getType());
            ivh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Html5Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", item.getUrl());
                    intent.putExtra("bundle", bundle);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof ImageViewHolder) {
            ImageViewHolder ivh = (ImageViewHolder) holder;
            Glide.with(mContext).load(item.getUrl()).into(ivh.image);
        } else if (holder.itemView instanceof TextView) {
            ((TextView) holder.itemView).setText(item.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;

        @Bind(R.id.type)
        TextView type;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView image;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemComparator implements Comparator<Item> {
        static Map<String, Integer> priorityMap = new HashMap<>();

        static {
            priorityMap.put("date", 1);
            priorityMap.put("福利", 2);
            priorityMap.put("Android", 3);
            priorityMap.put("iOS", 4);
            priorityMap.put("App", 5);
            priorityMap.put("瞎推荐", 6);
            priorityMap.put("休息视频", 7);
        }

        @Override
        public int compare(Item lhs, Item rhs) {
            Integer lp = priorityMap.get(lhs.getType());
            Integer rp = priorityMap.get(rhs.getType());
            return lp == null || rp == null ? 0 : lp - rp;
        }
    }
}
