package com.exmaple.funweather;

import android.widget.TextView;

/**
 * Created by EthanWalker on 2017/6/16.
 */

public class ProvinceCityCounty {
    private String province;
    private String city;
    private String county ;
    private int type;

    public ProvinceCityCounty(String province, String city, String county, int type) {
        this.province = province;
        this.city = city;
        this.county = county;
        this.type = type;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        county = county;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}