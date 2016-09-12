package com.kunkka.gank.model;

import com.kunkka.gank.pojo.HttpResult;

import rx.functions.Func1;

/**
 * Created by kunkka on 16/7/31.
 */

public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    @Override
    public T call(HttpResult<T> result) {
        if (result.isError()) {
            throw new ApiException();
        }
        return result.getResults();
    }
}
