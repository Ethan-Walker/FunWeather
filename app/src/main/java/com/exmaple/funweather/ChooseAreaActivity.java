package com.exmaple.funweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.exmaple.funweather.db.County;
import com.exmaple.funweather.gson.Weather;
import com.exmaple.funweather.search.ChsSearch;
import com.exmaple.funweather.util.ActivityCollector;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ChooseAreaActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        ChsSearch.countyLocDataList = DataSupport.findAll(County.class);

   /*     SharedPreferences preferences = getSharedPreferences("weather", Context.MODE_PRIVATE);
        if (preferences.getString("weatherData", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        ActivityCollector.removeActivity(this);
    }
}
