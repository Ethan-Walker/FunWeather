package com.exmaple.funweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class Suggestion {

    @SerializedName("comf")
    private Comfort comfort;

    @SerializedName("cw")
    private CarWash carWash;

    private Sport sport;

    /**
     * 紫外线
     */
    private UV uv;

    public class Comfort{

        @SerializedName("brf")
        String briefMsg;

        @SerializedName("txt")
        String info;

        public String getBriefMsg() {
            return briefMsg;
        }

        public void setBriefMsg(String briefMsg) {
            this.briefMsg = briefMsg;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
    public class CarWash{
        @SerializedName("brf")
        String briefMsg;

        @SerializedName("txt")
        String info;

        public String getBriefMsg() {
            return briefMsg;
        }

        public void setBriefMsg(String briefMsg) {
            this.briefMsg = briefMsg;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
    public class Sport{
        @SerializedName("brf")
        String briefMsg;

        @SerializedName("txt")
        String info;

        public String getBriefMsg() {
            return briefMsg;
        }

        public void setBriefMsg(String briefMsg) {
            this.briefMsg = briefMsg;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
    public class UV{
        @SerializedName("brf")
        String briefMsg;

        @SerializedName("txt")
        String info;

        public String getBriefMsg() {
            return briefMsg;
        }

        public void setBriefMsg(String briefMsg) {
            this.briefMsg = briefMsg;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public Comfort getComfort() {
        return comfort;
    }

    public void setComfort(Comfort comfort) {
        this.comfort = comfort;
    }

    public CarWash getCarWash() {
        return carWash;
    }

    public void setCarWash(CarWash carWash) {
        this.carWash = carWash;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public UV getUv() {
        return uv;
    }

    public void setUv(UV uv) {
        this.uv = uv;
    }
}
