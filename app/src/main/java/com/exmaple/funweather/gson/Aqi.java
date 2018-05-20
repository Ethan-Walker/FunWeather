package com.exmaple.funweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class Aqi {

    @SerializedName("city")
    private City city;

    public class City{
        @SerializedName("aqi")
        private String aqi;

        @SerializedName("pm25")
        private String pm25;

        @SerializedName("qlty")
        private String quality;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
