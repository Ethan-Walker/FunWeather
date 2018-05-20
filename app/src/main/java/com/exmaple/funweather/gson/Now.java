package com.exmaple.funweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class Now {

    @SerializedName("cond")
    private More more;

    public class More{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    @SerializedName("tmp")
    private String temperature;

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
