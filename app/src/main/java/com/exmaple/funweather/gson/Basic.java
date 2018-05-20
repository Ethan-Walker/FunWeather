package com.exmaple.funweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    private String weatherId;

    private Update update;

    public class Update{
        /**
         * 更新时间
         */
        @SerializedName("loc")
        private String updateLocTime;

        public String getUpdateLocTime() {
            return updateLocTime;
        }

        public void setUpdateLocTime(String updateLocTime) {
            this.updateLocTime = updateLocTime;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
}
