package com.exmaple.funweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.exmaple.funweather.db.City;
import com.exmaple.funweather.db.County;
import com.exmaple.funweather.db.User;
import com.exmaple.funweather.fuit.ChangeBackImg;
import com.exmaple.funweather.gson.Aqi;
import com.exmaple.funweather.gson.Basic;
import com.exmaple.funweather.gson.Forecast;
import com.exmaple.funweather.gson.Now;
import com.exmaple.funweather.gson.Suggestion;
import com.exmaple.funweather.gson.Weather;
import com.exmaple.funweather.service.AutoUpdateService;
import com.exmaple.funweather.util.ActivityCollector;
import com.exmaple.funweather.util.HttpUtil;
import com.exmaple.funweather.util.MyApplication;
import com.exmaple.funweather.util.Utility;
import com.exmaple.funweather.validate.LoginActivity;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

import static com.exmaple.funweather.service.AutoUpdateService.OFF;
import static com.exmaple.funweather.service.AutoUpdateService.ON;

public class WeatherActivity extends AppCompatActivity {

    private TextView titleLocation;
    private TextView nowDegree;
    private TextView lastUpdateTime;
    private TextView nowInfo;
    private TextView aqiNumber;
    private TextView airQuality;
    private TextView pm25;
    private LinearLayout forecastLayout;
    private TextView comfortSuggestion;
    private TextView carWashSuggestion;
    private TextView sportSuggestion;
    private TextView uvSuggestion;
    private TextView forecastDate;
    private TextView forecastInfo;
    private TextView forecastScaleTemp;
    private TextView forecastWindDir;
    private TextView forecastWindSc;
    private ScrollView scrollView;
    private ImageView dailyImg;
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Weather weather;
    private String dailyUrl;
    private ImageButton navLocation;
    private TextView isLocating;
    private NavigationView navigationView;
    private String weatherId;  // 就是城市的ID
    private CircleImageView personalImg;
    private static final String TAG = "WeatherActivity";
    private static final String BACK_IMG_URL = "http://guolin.tech/api/bing_pic";

    private User loginedUser;
    public static String url = "https://free-api.heweather.com/v5/weather";
    public static final String key = "cd3d10c1d45d484f8479424518b78f85";
    public static String guolinUrl = "http://www.guolin.tech/api/china";
    public static String cityListUrl = "https://search.heweather.com/find";
    // ?location=莲湖&key=


    public static User defaultUser = new User("ethan", "123456", "哈哈哈哈哈", "13186032132", "1169091421@qq.com", String.valueOf(R.drawable.a), BACK_IMG_URL);
    private LocationClient locationClient;
    private BDLocation bdLocation;
    private ImageButton settings;
    private DrawerLayout drawerLayout;

    public List<String> permissionList = new ArrayList<>();
    private RelativeLayout relativeLayout;
    private TextView navUsername;
    private FloatingActionButton addCity;
    private String username;

    private boolean hasLogined() {
        SharedPreferences preferences = getSharedPreferences("logined_user", MODE_PRIVATE);
        if (!preferences.getBoolean("has_logined", false)) {
            return false;
        }
        username = preferences.getString("username", null);
        return true;
    }

    private boolean isFirstLocate() {
        SharedPreferences preferences = getSharedPreferences("is_first_locate", MODE_PRIVATE);
        if (preferences.getBoolean("is_first_locate", true)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ActivityCollector.removeAll();

        ActivityCollector.addActivity(this);
        MyApplication.activity = this;
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(new MyLocationListener());
        isLocating = (TextView) findViewById(R.id.is_locating);
        mContext = this;

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        lastUpdateTime = (TextView) findViewById(R.id.last_update_time);
        titleLocation = (TextView) findViewById(R.id.title_location);
        nowDegree = (TextView) findViewById(R.id.now_degree_txt);
        nowInfo = (TextView) findViewById(R.id.now_info_txt);
        aqiNumber = (TextView) findViewById(R.id.aqi_number);
        airQuality = (TextView) findViewById(R.id.air_quality);
        pm25 = (TextView) findViewById(R.id.pm25);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        comfortSuggestion = (TextView) findViewById(R.id.comfort_txt);
        carWashSuggestion = (TextView) findViewById(R.id.car_wash_txt);
        sportSuggestion = (TextView) findViewById(R.id.sport_txt);
        uvSuggestion = (TextView) findViewById(R.id.uv_txt);
        scrollView = (ScrollView) findViewById(R.id.weather_layout);
        dailyImg = (ImageView) findViewById(R.id.daily_img);
        navLocation = (ImageButton) findViewById(R.id.nav_location);
        settings = (ImageButton) findViewById(R.id.settings);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relativeLayout = (RelativeLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_header_relative_layout);
        navUsername = (TextView) relativeLayout.findViewById(R.id.nav_username);
        personalImg = (CircleImageView) relativeLayout.findViewById(R.id.navigation_personal_img);
//        addCity = (FloatingActionButton) findViewById(R.id.add_city);
        if (isFirstLocate()) {
            init();
            SharedPreferences preferences = getSharedPreferences("is_first_locate", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_first_locate", false);
            editor.apply();
        }

        scrollView.setVisibility(View.INVISIBLE);
        isLocating.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLocating.setVisibility(View.VISIBLE);
                isLocating.setText("正在更新...");
                // weather 为空
                if (weatherId != null) {
                    sendReqWeatherDataById(weatherId);
                }
            }
        });

        titleLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChooseAreaActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.END);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_my_logout:
                        SharedPreferences preferences = getSharedPreferences("logined_user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("has_logined", false);
                        editor.apply();
                        ActivityCollector.removeAll();
                        Intent intent = new Intent(WeatherActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_change_back_img:
                        Intent intent2 = new Intent(WeatherActivity.this, ChangeBackImg.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_switch_service:
                        Intent o = new Intent(WeatherActivity.this, AutoUpdateService.class);

                        if (AutoUpdateService.flag == ON) {
                            AutoUpdateService.flag = OFF;
                            item.setTitle("打开自动更新");
                            stopService(o);
                        } else {
                            AutoUpdateService.flag = ON;
                            item.setTitle("关闭自动更新");
                            startService(o);
                        }
                        break;

                    default:
                        break;
                }
                /*return false 使得点击完之后，选中效果 取消*/
                return false;
            }
        });

        personalImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, PersonActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        weatherId = intent.getStringExtra("weatherId");
        if (weatherId != null) {
            scrollView.setVisibility(View.INVISIBLE);
            sendReqWeatherDataById(weatherId);
        } else {
            SharedPreferences preferences = getSharedPreferences("weather", MODE_PRIVATE);
            String weatherData = preferences.getString("weatherData", null);
            if (weatherData != null) {
                weather = Utility.parseWeatherData(weatherData);
                show(weather);
            }
        }
        navLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocating.setText("正在定位...");
                isLocating.setVisibility(View.VISIBLE);
                reqLocation();
            }
        });
    }


    public void init() {
        if (ContextCompat.checkSelfPermission(MyApplication.getmContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MyApplication.getmContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MyApplication.getmContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionList.isEmpty()) {
            Log.e(TAG,"permissionList 为空");
            isLocating.setVisibility(View.VISIBLE);
            reqLocation();
        } else {
            ActivityCompat.requestPermissions(WeatherActivity.this, permissionList.toArray(new String[permissionList.size()]), 1);
            Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
            startService(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults != null && grantResults.length > 0) {
                    for (int res : grantResults) {
                        if (res != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MyApplication.getmContext(), "定位权限不足", Toast.LENGTH_SHORT).show();

                        }
                    }
                    permissionList.clear();
                    isLocating.setVisibility(View.VISIBLE);
                    reqLocation();
                } else {
                    finish();
                    Toast.makeText(MyApplication.getmContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void initUser() {
        String imgUrl = loginedUser.getHeadImgUrl();
        String backImgUrl = loginedUser.getBackImgUrl();
        if (imgUrl == null) {
            imgUrl = defaultUser.getHeadImgUrl();
            loginedUser.setHeadImgUrl(defaultUser.getHeadImgUrl());
        }
        if (backImgUrl == null) {
            backImgUrl = loginedUser.getBackImgUrl();
            loginedUser.setBackImgUrl(defaultUser.getBackImgUrl());
        }
        loginedUser.save();
        String username = loginedUser.getUsername();

        if (username != null) {
            navUsername.setText(username);
        }
        if (imgUrl != null) {
            if (Utility.isDigit(imgUrl)) {
                Glide.with(getApplicationContext()).load(Integer.valueOf(imgUrl)).into(personalImg);

            } else {
                Glide.with(getApplicationContext()).load(imgUrl).into(personalImg);

            }
        }
        if (backImgUrl != null) {
            if (Utility.isDigit(backImgUrl)) {
                Glide.with(this).load(Integer.valueOf(backImgUrl)).into(dailyImg);

            } else {
                sendReqDailyImg(BACK_IMG_URL);
            }
        }

    }


    private void reqLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setIsNeedAddress(true);

        locationClient.setLocOption(option);
        locationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            runOnUiThread(() -> {
                isLocating.setVisibility(View.VISIBLE);
            });
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String district = bdLocation.getDistrict();
            String county = bdLocation.getCountry();

            Log.e(TAG,province);
            Log.e(TAG, "定位1");

            Log.e(TAG, bdLocation.getAddrStr());
            Log.e(TAG, "district=" + district);
            Log.e(TAG, "county" + county);

            if (province.endsWith("省") || province.endsWith("市") || province.endsWith("区")) {
                int length = province.length();
                province = province.substring(0, length - 1);
            }

            if (city.contains("市")) {
                int length = city.length();
                city = city.substring(0, length - 1);
            }
            if (district.contains("区") || district.contains("县")) {
                int length = district.length();
                district = district.substring(0, length - 1);
            }

            getCityId(district, WeatherActivity.cityListUrl);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

    }

    public void getCityId(String district, String url) {
        HttpUtil.sendOkHttpReq(url + "?location=" + district + "&key=" + WeatherActivity.key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获取城市 id 失败");
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
                    isLocating.setVisibility(View.INVISIBLE);
                });
                locationClient.stop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                String cityId = Utility.parseGetCityId(string);
                Log.e(TAG, cityId);
                weatherId = cityId;
                sendReqWeatherDataById(weatherId);
                runOnUiThread(()->{
                    isLocating.setVisibility(View.INVISIBLE);
                });
                locationClient.stop();

                sendReqWeatherDataById(weatherId);
            }
        });

    }

    ;


    void sendReqDailyImg(String url) {
        HttpUtil.sendOkHttpReq(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String actualUrl = response.body().string();
                runOnUiThread(() -> {
                    Glide.with(mContext).load(actualUrl).into(dailyImg);
                });
            }
        });
    }

    // cityId
    void sendReqWeatherDataById(String cityId) {
        HttpUtil.sendOkHttpReq(url + "?city=" + cityId + "&" + "key=" + key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    isLocating.setText("更新失败,请检查网络");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();  // 不能调用2次
//                response.body().string();
                new Thread(() -> {
                    SharedPreferences preferences = getSharedPreferences("weather", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("weatherData", responseData);
                    editor.apply();
                }).start();

//                Log.e(TAG, responseData);

                weather = Utility.parseWeatherData(responseData);
                runOnUiThread(() -> {
                    show(weather);
                    swipeRefreshLayout.setRefreshing(false);
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = dateFormat.format(date);
                    lastUpdateTime.setText("更新时间 " + time.toString().substring(time.indexOf(" ") + 1, time.lastIndexOf(":")));
//                  Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    isLocating.setVisibility(View.INVISIBLE);
                });

            }
        });
    }

    void show(Weather weather) {

        Basic basic = weather.getBasic();
        Aqi aqi = weather.getAqi();
        String status = weather.getStatus();
        Now now = weather.getNow();
        Suggestion suggestion = weather.getSuggestion();
        List<Forecast> forecasts = weather.getForecasts();

        //title
        if (basic != null) {
            titleLocation.setText(basic.getCityName());
            String updateTimeData = basic.getUpdate().getUpdateLocTime();
        }

        // now
        if (now != null) {
            nowDegree.setText(now.getTemperature() + "℃");
            nowInfo.setText(now.getMore().getInfo());
        }


        // aqi 空气质量
        if (aqi != null) {
            aqiNumber.setText(aqi.getCity().getAqi());
            airQuality.setText(aqi.getCity().getQuality());
            pm25.setText(aqi.getCity().getPm25());
        }


        // forecast 预报
        forecasts = weather.getForecasts();

        forecastLayout.removeAllViews();
        if (forecasts != null) {
            for (int i = 0; i < forecasts.size(); i++) {
                Forecast forecast = forecasts.get(i);
                View v = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                forecastDate = (TextView) v.findViewById(R.id.date_txt);
                forecastInfo = (TextView) v.findViewById(R.id.info_txt);
/*            forecastMaxTemperature = (TextView) v.findViewById(R.id.max_temperature);
            forecastMinTemperature = (TextView) v.findViewById(R.id.min_temperature);*/
                forecastScaleTemp = (TextView) v.findViewById(R.id.scale_temperature);
                forecastWindDir = (TextView) v.findViewById(R.id.wind_direction);
                forecastWindSc = (TextView) v.findViewById(R.id.wind_sc);
                if (i == 0) {
                    forecastDate.setText("今天");

                } else if (i == 1) {
                    forecastDate.setText("明天");
                } else if (i == 2) {
                    forecastDate.setText("后天");
                }
                forecastInfo.setText("白天: " + forecast.getMore().getDaySimpleDesc() + "\n晚上: " + forecast.getMore().getNightSimpleDesc());

                forecastScaleTemp.setText(forecast.getTemperature().getMin() + "℃~" + forecast.getTemperature().getMax() + "℃");
                forecastWindDir.setText(forecast.getWind().getDirection());
                String degree = forecast.getWind().getDegree();
                if (!degree.contains("风")) {
                    degree += "级";
                }
                forecastWindSc.setText(degree);
                forecastLayout.addView(v);
            }
        }


        // suggestion
        if (suggestion != null) {
            comfortSuggestion.setText(suggestion.getComfort().getBriefMsg() + ": " + suggestion.getComfort().getInfo());
            carWashSuggestion.setText(suggestion.getCarWash().getBriefMsg() + ": " + suggestion.getCarWash().getInfo());
            sportSuggestion.setText(suggestion.getSport().getBriefMsg() + ": " + suggestion.getSport().getInfo());
            uvSuggestion.setText(suggestion.getUv().getBriefMsg() + ": " + suggestion.getUv().getInfo());

        }

        scrollView.setVisibility(View.VISIBLE);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (hasLogined()) {
            List<User> users = DataSupport.where("username=?", username).find(User.class);
            loginedUser = users.get(0);
            initUser();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(Gravity.END);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
