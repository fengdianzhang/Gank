package com.kunkka.gank.pojo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.kunkka.gank.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by kunkka on 16/7/30.
 */

public class Item implements Comparable<Item> {
    private static Map<String, Integer> priorityMap = new HashMap<>();

    static {
        priorityMap.put("date", 1);
        priorityMap.put("福利", 2);
        priorityMap.put("Android", 3);
        priorityMap.put("iOS", 4);
        priorityMap.put("前端", 5);
        priorityMap.put("App", 6);
        priorityMap.put("瞎推荐", 7);
        priorityMap.put("休息视频", 8);
        priorityMap.put("拓展资源", 9);
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    @SerializedName("_id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("desc")
    private String title;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("source")
    private String source;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;
    @SerializedName("used")
    private boolean used;
    @SerializedName("who")
    private String author;
    @SerializedName("date")
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(@NonNull Item another) {
        if (this.getDateMills() == another.getDateMills()) {
            return this.getPriority() - another.getPriority();
        } else {
            return this.getDateMills() < another.getDateMills() ? 1 : -1;
        }
    }

    private long getDateMills() {
        if (!TextUtils.isEmpty(date)) {
            try {
                return SDF.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private int getPriority() {
        Integer priority = priorityMap.get(type);
        return priority == null ? Integer.MAX_VALUE : priority;
    }

    public int getIconResId() {
        switch (type) {
            case "Android":
                return R.drawable.logo_android;
            case "iOS":
                return R.drawable.logo_ios;
            case "前端":
                return R.drawable.logo_html5;
            case "App":
                return R.drawable.logo_chrome;
            case "拓展资源":
                return R.drawable.logo_books;
            case "休息视频":
                return R.drawable.logo_video;
            case "瞎推荐":
                return R.drawable.logo_recommend;
            default:
                return R.drawable.logo_books;
        }
    }
}
