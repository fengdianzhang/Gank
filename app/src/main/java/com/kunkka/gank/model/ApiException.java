package com.kunkka.gank.model;

/**
 * Created by kunkka on 16/7/31.
 */

public class ApiException extends RuntimeException {
    public ApiException() {
        super("Request is error.");
    }
}
