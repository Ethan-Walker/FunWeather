package com.exmaple.funweather.util;

import com.exmaple.funweather.db.City;
import com.exmaple.funweather.db.County;
import com.exmaple.funweather.db.Province;
import com.exmaple.funweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class Utility {

    /**
     * 解析服务器返回的省级数据,存入数据库中
     */
    public static boolean parseProvince(String data) {
        JSONArray array;
        try {
            array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                JSONObject provinceObj = array.getJSONObject(i);
                Province province = new Province();
                province.setId(provinceObj.getInt("id"));
                province.setProvinceName(provinceObj.getString("name"));
                province.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String parseGetCityId(String data) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(data);
            JSONArray array = obj.getJSONArray("HeWeather6");
            JSONObject innerObj = array.getJSONObject(0);
            JSONArray basicArray = innerObj.getJSONArray("basic");
            JSONObject threeObj = basicArray.getJSONObject(0);
            return threeObj.getString("cid");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析服务器返回的市级数据
     */
    public static boolean parseCity(String data, int provinceId) {
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                City city = new City();
                city.setId(obj.getInt("id"));
                city.setCityName(obj.getString("name"));
                city.setProvinceId(provinceId);
                city.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析返回的县级数据
     */
    public static boolean parseCounty(String data, int cityId, int provinceId) {
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                County county = new County();
                county.setCityId(cityId);
                county.setProvinceId(provinceId);
                county.setCountyName(obj.getString("name"));
                county.setWeatherId(obj.getString("weather_id"));
                county.save();
            }
            return true;
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return false;
    }

    public static Weather parseWeatherData(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray array = obj.getJSONArray("HeWeather5");
            String resData = array.getJSONObject(0).toString();
            return new Gson().fromJson(resData, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
