package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-08-21.
 */

public class BeaconAirValue {

    @SerializedName("airvalue")
    @Expose
    private List<Airvalue> airvalue = null;

    public List<Airvalue> getAirvalue() {
        return airvalue;
    }

    public void setAirvalue(List<Airvalue> airvalue) {
        this.airvalue = airvalue;
    }

    public class Airvalue {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("m_time")
        @Expose
        private String mTime;
        @SerializedName("dust")
        @Expose
        private String dust;
        @SerializedName("gas")
        @Expose
        private String gas;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMTime() {
            return mTime;
        }

        public void setMTime(String mTime) {
            this.mTime = mTime;
        }

        public String getDust() {
            return dust;
        }

        public void setDust(String dust) {
            this.dust = dust;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

    }
}
