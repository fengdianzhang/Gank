package com.kunkka.gank.pojo;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.kunkka.gank.GankApplication;
import com.kunkka.gank.tools.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunkka on 17/4/11.
 */

public class Type {
    private transient static final Map<String, Type> TYPE_MAP = new LinkedHashMap<>();

    static {
        String json = Utils.loadJSONFromAsset(GankApplication.getApplication(), "types.json");
        List<Type> types = new Gson().fromJson(json, new TypeToken<List<Type>>() {}.getType());
        for (Type type : types) {
            TYPE_MAP.put(type.getName(), type);
        }
    }

    @SerializedName("name")
    private String name;
    @SerializedName("order")
    private int order;
    @SerializedName("logo")
    private String logo;

    private transient boolean enable = true;

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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                ", order=" + order +
                ", logo='" + logo + '\'' +
                ", enable=" + enable +
                '}';
    }

    public int getLogoResId(Context context) {
        return context.getResources().getIdentifier(logo, "drawable", context.getPackageName());
    }

    public static Map<String, Type> getTypeMap() {
        return TYPE_MAP;
    }
}
