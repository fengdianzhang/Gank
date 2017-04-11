package com.kunkka.gank.pojo;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kunkka on 17/4/11.
 */

public class Type {
    @SerializedName("name")
    private String name;
    @SerializedName("order")
    private int order;
    @SerializedName("logo")
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getLogoResId(Context context) {
        return context.getResources().getIdentifier(logo, "drawable", context.getPackageName());
    }
}
