package com.exmaple.funweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by EthanWalker on 2017/6/12.
 */

public class County extends DataSupport {

    private int id;
    private String countyName;
    private int cityId;
    private int provinceId;
    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "County{" +
                "id=" + id +
                ", countyName='" + countyName + '\'' +
                ", cityId=" + cityId +
                ", provinceId=" + provinceId +
                ", weatherId='" + weatherId + '\'' +
                '}';
    }
}
