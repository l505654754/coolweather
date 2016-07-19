package com.example.coolweather.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by yang on 2016/7/17.
 */
public class MyApplication extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        context = getApplicationContext();
      //  L.isDebug = false;
        super.onCreate();


    }

    public static Context getContext() {
        return context;
    }
}
