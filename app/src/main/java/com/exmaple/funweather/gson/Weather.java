package com.exmaple.funweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class Weather {

    private String status;
    private Aqi aqi;
    private Basic basic;
    private Now now;
    private Suggestion suggestion;

    @SerializedName("daily_forecast")
    private List<Forecast> forecasts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }
}
