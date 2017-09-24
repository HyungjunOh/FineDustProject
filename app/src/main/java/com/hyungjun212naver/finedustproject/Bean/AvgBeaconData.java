package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-09-24.
 */

public class AvgBeaconData {

    @SerializedName("AvgAirValue")
    @Expose
    private List<AvgAirValue> avgAirValue = null;

    public List<AvgAirValue> getAvgAirValue() {
        return avgAirValue;
    }

    public void setAvgAirValue(List<AvgAirValue> avgAirValue) {
        this.avgAirValue = avgAirValue;
    }

    public class AvgAirValue {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("m_time")
        @Expose
        private String mTime;
        @SerializedName("avg_dust")
        @Expose
        private String avgDust;
        @SerializedName("avg_gas")
        @Expose
        private String avgGas;

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

        public String getAvgDust() {
            return avgDust;
        }

        public void setAvgDust(String avgDust) {
            this.avgDust = avgDust;
        }

        public String getAvgGas() {
            return avgGas;
        }

        public void setAvgGas(String avgGas) {
            this.avgGas = avgGas;
        }

    }

}