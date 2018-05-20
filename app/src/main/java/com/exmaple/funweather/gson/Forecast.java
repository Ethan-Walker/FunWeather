package com.exmaple.funweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class Forecast {
    @SerializedName("cond")
    private More more;

    private String date;

    @SerializedName("tmp")
    private Temperature temperature;

    private Wind wind;

    public class More{
        /**
         * 白天天气 ：例，晴
         */
        @SerializedName("txt_d")
        String daySimpleDesc;

        @SerializedName("txt_n")
        String nightSimpleDesc;

        public String getDaySimpleDesc() {
            return daySimpleDesc;
        }

        public void setDaySimpleDesc(String daySimpleDesc) {
            this.daySimpleDesc = daySimpleDesc;
        }

        public String getNightSimpleDesc() {
            return nightSimpleDesc;
        }

        public void setNightSimpleDesc(String nightSimpleDesc) {
            this.nightSimpleDesc = nightSimpleDesc;
        }
    }
    public class Temperature{

        String min;
        String max;

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }
    }
    public class Wind{
        /**
         * 风向
         */
        @SerializedName("dir")
        private String direction;

        /**
         * 风力等级
         */
        @SerializedName("sc")
        private String degree;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
