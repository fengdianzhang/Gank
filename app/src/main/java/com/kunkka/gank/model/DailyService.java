package com.kunkka.gank.model;

import com.kunkka.gank.pojo.Item;
import com.kunkka.gank.pojo.HttpResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by kunkka on 16/7/31.
 */

public interface DailyService {
    @GET("day/history")
    Observable<HttpResult<List<String>>> getHistoryDays();

    @GET("day/{year}/{month}/{day}")
    Observable<HttpResult<Map<String, List<Item>>>> getDaily(@Path("year") int year, @Path("month") int month, @Path("day") int day);
}
