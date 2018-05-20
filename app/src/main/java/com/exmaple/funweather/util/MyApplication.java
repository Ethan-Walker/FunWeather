package com.exmaple.funweather.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by EthanWalker on 2017/6/14.
 */

public class MyApplication extends Application {

    private static Context mContext;

    public static Activity activity;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LitePalApplication.initialize(mContext);
    }

    public static Context getmContext() {
        return mContext;
    }
}
