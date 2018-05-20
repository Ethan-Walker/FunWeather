package com.exmaple.funweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.exmaple.funweather.WeatherActivity;
import com.exmaple.funweather.gson.Weather;
import com.exmaple.funweather.util.HttpUtil;
import com.exmaple.funweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by EthanWalker on 2017/6/15.
 */

public class AutoUpdateService extends Service {

    private static final String TAG = "AutoUpdateService";
    public static int ON = 1;
    public static int OFF = 2;
    public static int flag = ON;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);

        if (flag == ON) {
            updateBingImg();
            updateWeather();
            int delay = 5 * 60 * 60 * 1000;
            long triggerTime = SystemClock.elapsedRealtime() + delay;

            manager.cancel(pendingIntent);    //  删除之前与pendingIntent绑定 的服务
            manager.set(AlarmManager.ELAPSED_REALTIME, triggerTime, pendingIntent);
        } else {
            manager.cancel(pendingIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void updateWeather() {
        SharedPreferences preferences = getSharedPreferences("weather", MODE_PRIVATE);
        String weatherData = preferences.getString("weatherData", null);
        if (weatherData != null) {
            Weather weather = Utility.parseWeatherData(weatherData);
            String weatherId = weather.getBasic().getWeatherId();
            String url = WeatherActivity.url + "?city=" + weatherId + "&key=" + WeatherActivity.key;

            HttpUtil.sendOkHttpReq(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("weatherData", responseData);
                    editor.apply();
                }
            });
        }
    }

    private void updateBingImg() {
        String reqUrl = "http://guolin.tech/api/bing_pc";
        HttpUtil.sendOkHttpReq(reqUrl, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dailyUrl = response.body().string();
                SharedPreferences preferences = getSharedPreferences("dailyImg", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("dailyImgUrl", null);
                editor.apply();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
