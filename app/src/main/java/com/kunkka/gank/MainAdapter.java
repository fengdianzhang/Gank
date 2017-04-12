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

import com.kunkka.gank.pojo.Item;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kunkka on 16/7/26.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mInflater;
    private final List<Item> mItems = new ArrayList<>();
    private final List<Item> mAllItems = new ArrayList<>();
    private final Collection<String> mFilterNames = new HashSet<>();
    private final Context mContext;

    public MainAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void addItems(Collection<Item> items) {
        mAllItems.addAll(items);
        for (Item item : items) {
            if (!mFilterNames.contains(item.getType())) {
                mItems.add(item);
            }
        }
        Collections.sort(mItems);
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
            ivh.icon.setImageResource(item.getIconResId());
            ivh.author.setText(item.getAuthor());
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
            Picasso.with(mContext)
                    .load(item.getUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(ivh.image);
            ivh.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                }
            });
        } else if (holder.itemView instanceof TextView) {
            ((TextView) holder.itemView).setText(item.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public String getCurrTitle(int firstVisiblePosition) {
        Item item = mItems.get(firstVisiblePosition);
        try {
            return new SimpleDateFormat("M月d日 EEEE", Locale.CHINA)
                    .format(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(item.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean filter(Set<String> filterNames) {
        if (mFilterNames.equals(filterNames)) {
            return false;
        }
        mFilterNames.clear();
        mFilterNames.addAll(filterNames);
        mItems.clear();
        for (Item item : mAllItems) {
            if (!mFilterNames.contains(item.getType())) {
                mItems.add(item);
            }
        }
        Collections.sort(mItems);
        notifyDataSetChanged();
        return true;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.author)
        TextView author;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView image;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
