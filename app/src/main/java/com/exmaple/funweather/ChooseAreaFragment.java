package com.exmaple.funweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.exmaple.funweather.db.City;
import com.exmaple.funweather.db.County;
import com.exmaple.funweather.db.Province;
import com.exmaple.funweather.gson.Weather;
import com.exmaple.funweather.search.ChsSearch;
import com.exmaple.funweather.util.ActivityCollector;
import com.exmaple.funweather.util.HttpUtil;
import com.exmaple.funweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.R.id.list;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class ChooseAreaFragment extends Fragment {

    private int PROVINCE_LEVEL = 1;
    private int CITY_LEVEL = 2;
    private int COUNTY_LEVEL = 3;
    private int FAIL_LEVEL = 4;
    private int SURE_LEVEL = 5;
    private int SEARCH_LEVEL = 6;

    private int currentLevel = PROVINCE_LEVEL;
    private int flag = 0;
    public static boolean is_search = false;
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;
    private Weather sureWeather;
    private SearchView searchView;
    private List<Province> provinceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<County> countyList = new ArrayList<>();
    private List<ProvinceCityCounty> dataList = new ArrayList<>();
    private List<ProvinceCityCounty> oldDataList = new ArrayList<>();
    private ProvinceCityCounty selectedItemWhenSearch;

    private TextView titleText;
    private Button back;
    private ListView listView;
    private MyArrayAdapter adapter;

    private ImageView immediateLocate;

    private static final int TYPE_PROVINCE = 0x111;
    private static final int TYPE_CITY = 0x222;
    private static final int TYPE_COUNTY = 0x333;

    private List<County> searchResult;
    private static final String TAG = "ChooseAreaFragment";
    private LocationClient client;
    private BDLocation bdLocation;
    private int oldLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) v.findViewById(R.id.title_text);
        back = (Button) v.findViewById(R.id.back);
        immediateLocate = (ImageView) v.findViewById(R.id.immediate_locate);
        listView = (ListView) v.findViewById(R.id.list_view);
        searchView = (SearchView) v.findViewById(R.id.search_city);
        adapter = new MyArrayAdapter(getActivity(), R.layout.location_ltem, dataList);
        listView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProvinces();

        client = new LocationClient(getActivity());
        client.registerLocationListener(new MyLocationListener());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == PROVINCE_LEVEL) {
                    if (provinceList != null) {
                        selectedProvince = provinceList.get(position);
                        showCities();
                    }
                } else if (currentLevel == CITY_LEVEL) {
                    selectedCity = cityList.get(position);
                    showCounties();
                } else if (currentLevel == COUNTY_LEVEL) {
                    selectedCounty = countyList.get(position);
                    Activity activity = getActivity();
                    Intent intent = new Intent(activity, WeatherActivity.class);
                    intent.putExtra("weatherId", selectedCounty.getWeatherId());
                    activity.startActivity(intent);
                    ActivityCollector.removeActivity(activity);
                } else if (currentLevel == SURE_LEVEL) {
                    Activity activity = getActivity();
                    Intent intent = new Intent(activity, WeatherActivity.class);
                    intent.putExtra("weatherId", sureWeather.getBasic().getWeatherId());
                    startActivity(intent);
                    ActivityCollector.removeActivity(activity);
                } else if (currentLevel == SEARCH_LEVEL) {
                    selectedItemWhenSearch = dataList.get(position);
                    String provinceName = selectedItemWhenSearch.getProvince();
                    String cityName = selectedItemWhenSearch.getCity();
                    String countyName = selectedItemWhenSearch.getCounty();
                    if (provinceName.endsWith("省") || provinceName.endsWith("市") || provinceName.endsWith("区")) {
                        int length = provinceName.length();
                        provinceName = provinceName.substring(0, length - 1);
                    }

                    if (cityName.contains("市")) {
                        int length = cityName.length();
                        cityName = cityName.substring(0, length - 1);
                    }
                    if (countyName.contains("区") || countyName.contains("县")) {
                        int length = countyName.length();
                        countyName = countyName.substring(0, length - 1);
                    }
                    Activity activity = getActivity();
//                    List<County> counties = DataSupport.where("countyName=?", countyName).find(County.class);d
                    HttpUtil.sendOkHttpReq(WeatherActivity.cityListUrl + "?location=" + countyName + "&key=" + WeatherActivity.key, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "获取城市 id 失败");
                            Intent intent = new Intent(activity, WeatherActivity.class);
                            activity.startActivity(intent);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            String cityId = Utility.parseGetCityId(string);
                            Log.e(TAG, "location: "+cityId);
                            Intent intent = new Intent(activity, WeatherActivity.class);
                            intent.putExtra("weatherId", cityId);

                            activity.startActivity(intent);
                            ActivityCollector.removeActivity(activity);
                        }
                    });
                }
            }
        });
        back.setOnClickListener((v) ->

        {
            if (currentLevel == COUNTY_LEVEL) {
                showCities();
            } else if (currentLevel == CITY_LEVEL) {
                showProvinces();
            } else if (currentLevel == PROVINCE_LEVEL) {
                getActivity().finish();
            } else {
                getActivity().finish();
            }
        });

        /**
         *  立即定位到当前位置
         */
        immediateLocate.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                requestLocate();
            }

        });
        searchView.setFocusable(true);
        searchView.requestFocus();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("请输入城市名");


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                Toast.makeText(getContext(), "queryTextFocusChanged", Toast.LENGTH_SHORT).show();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()

        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                is_search = true;
                if (!TextUtils.isEmpty(newText)) {
                    currentLevel = SEARCH_LEVEL;
                    searchResult = ChsSearch.search(newText);
                    dataList.clear();
                    if (searchResult.size() > 0) {
                        for (County county : searchResult) {
                            List<Province> provinces = DataSupport.where("id=?", String.valueOf(county.getProvinceId())).find(Province.class);
                            String provinceName = provinces.get(0).getProvinceName();
                            List<City> cities = DataSupport.where("id=?", String.valueOf(county.getProvinceId())).find(City.class);
                            String cityName = cities.get(0).getCityName();
                            int type = judgeType(provinceName, cityName, county.getCountyName());
                            Log.e(TAG, provinceName + "=>" + cityName + "=>" + county.getCountyName() + "=>type=" + type);
                            ProvinceCityCounty item = new ProvinceCityCounty(provinceName, cityName, county.getCountyName(), type);
                            dataList.add(item);
                        }
                    } else if (searchResult.size() == 0) {
                        ProvinceCityCounty item = new ProvinceCityCounty("", "", "未找到相关城市", MyArrayAdapter.IS_PROVINCE);
                        dataList.add(item);
                    }
                } else {
                    currentLevel = oldLevel;
                    dataList.clear();
                    dataList.addAll(oldDataList);
                }
                getActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    is_search = false;

                });
                return true;

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener()

        {
            @Override
            public boolean onClose() {
                is_search = false;
                Toast.makeText(getActivity(), "onClose", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    public void requestLocate() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        client.setLocOption(option);
        client.start();
    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.e(TAG, "bdLocation: " + bdLocation.getDistrict());
            if (bdLocation != null) {
                String province = bdLocation.getProvince();
                String city = bdLocation.getCity();
                String district = bdLocation.getDistrict();
                Log.e(TAG, bdLocation.getAddrStr());
                Log.e(TAG, district);


                String reqUrl = WeatherActivity.url + "?city=" + bdLocation.getLongitude() + "," + bdLocation.getLatitude() + "&key=" + WeatherActivity.key;

                Log.e(TAG, "reqUrl=" + reqUrl);

                int type = judgeType(province, city, district);
                ProvinceCityCounty item = new ProvinceCityCounty(province, city, district, type);
                HttpUtil.sendOkHttpReq(reqUrl, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Weather weather = Utility.parseWeatherData(responseData);
                        dataList.clear();
                        dataList.add(item);
                        getActivity().runOnUiThread(() -> {
                            currentLevel = SEARCH_LEVEL;
                            adapter.notifyDataSetChanged();
                        });
                    }
                });
            }
            client.stop();

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    private void showProvinces() {
        titleText.setText("中国"

        );
        back.setVisibility(View.VISIBLE);
        provinceList = DataSupport.findAll(Province.class);  // 先去数据库中查找，为空的话，去网页请求
        if (provinceList == null || provinceList.size() == 0) {
            queryFromServer("http://guolin.tech/api/china", TYPE_PROVINCE);
        } else {
            dataList.clear();
            for (Province province : provinceList) {
                /*不在搜索状态，只显示在 County 的位置*/
                ProvinceCityCounty item = new ProvinceCityCounty("", "", province.getProvinceName(), MyArrayAdapter.IS_PROVINCE);
                dataList.add(item);
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            oldLevel = currentLevel = PROVINCE_LEVEL;
            oldDataList.clear();
            oldDataList.addAll(dataList);
        }

    }

    // 由于是异步，观察是否显示列表为空
    // 当该方法执行完后，子线程才取得数据 ， 改善，子线程取完数据后，切换到主线程，再次调用该方法
    private void showCities() {
        back.setVisibility(View.VISIBLE);
        titleText.setText(selectedProvince.getProvinceName());

        cityList = DataSupport.where("provinceId= ?", String.valueOf(selectedProvince.getId())).find(City.class);

        if (cityList == null || cityList.size() == 0) {
            String url = new StringBuilder("http://guolin.tech/api/china/").append(selectedProvince.getId()).toString();
            queryFromServer(url, TYPE_CITY);
        } else {
            dataList.clear();
            for (int i = 0; i < cityList.size(); i++) {
                City city = cityList.get(i);

                ProvinceCityCounty item = new ProvinceCityCounty("", "", city.getCityName(), MyArrayAdapter.IS_CITY);
                dataList.add(item);
            }
            adapter.notifyDataSetChanged();
            oldLevel = currentLevel = CITY_LEVEL;
            listView.setSelection(0);
            oldDataList.clear();
            oldDataList.addAll(dataList);
        }
    }

    private void showCounties() {
        back.setVisibility(View.VISIBLE);
        titleText.setText(selectedCity.getCityName());

        countyList = DataSupport.where("cityId = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList == null || countyList.size() == 0) {
            String url = new StringBuilder("http://guolin.tech/api/china/").append(selectedProvince.getId()).append("/").append(selectedCity.getId()).toString();
            queryFromServer(url, TYPE_COUNTY);
        } else {
            dataList.clear();
            for (County county : countyList) {
                List<Province> tempList = DataSupport.where("id =?", String.valueOf(county.getProvinceId())).find(Province.class);
                Province province = tempList.get(0);
                List<City> tempList2 = DataSupport.where("id =?", String.valueOf(county.getCityId())).find(City.class);
                City city = tempList2.get(0);
                ProvinceCityCounty item = new ProvinceCityCounty("", "", county.getCountyName(), MyArrayAdapter.IS_COUNTY);
                dataList.add(item);
            }
            adapter.notifyDataSetChanged();
            oldLevel = currentLevel = COUNTY_LEVEL;
            listView.setSelection(0);
            oldDataList.clear();
            oldDataList.addAll(dataList);
        }

    }

    private void queryFromServer(String url, int type) {
        showProgressDialog();
        HttpUtil.sendOkHttpReq(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> {
                    closeProgressDialog();
                    Toast.makeText(getActivity(), "请求服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                getActivity().runOnUiThread(() -> {
                    closeProgressDialog();
                    if (type == TYPE_PROVINCE) {
                        Utility.parseProvince(responseData);
                        provinceList = DataSupport.findAll(Province.class);
                        showProvinces();
                    } else if (type == TYPE_CITY) {
                        Utility.parseCity(responseData, selectedProvince.getId());
                        cityList = DataSupport.where("provinceId = ?", String.valueOf(selectedProvince.getId())).find(City.class);
                        showCities();
                    } else if (type == TYPE_COUNTY) {
                        Utility.parseCounty(responseData, selectedCity.getId(), selectedProvince.getId());
                        countyList = DataSupport.where("cityId = ?", String.valueOf(selectedCity.getId())).find(County.class);
                        showCounties();
                    }
                });
            }
        });


    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();

    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private ProgressDialog progressDialog = null;


    @Override
    public void onDestroy() {
        super.onDestroy();
        client.stop();

    }

    public int judgeType(String provinceName, String cityName, String countyName) {
        if (provinceName.equals(countyName)) {
            return MyArrayAdapter.IS_PROVINCE;
        } else if (cityName.equals(countyName)) {
            return MyArrayAdapter.IS_CITY;
        } else {
            return MyArrayAdapter.IS_COUNTY;
        }
    }
}
