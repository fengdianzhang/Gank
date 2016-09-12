package com.kunkka.gank.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by kunkka on 16/7/30.
 */

public class HttpResult<T> {
    @SerializedName("error")
    private boolean error;
    @SerializedName("results")
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
