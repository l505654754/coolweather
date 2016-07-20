package com.example.coolweather.utils;

/**
 * Created by yang on 2016/7/19.
 */
public interface HttpCallbackListener {
    public void onFinish(String response);
    public void onError(Exception e);
}
