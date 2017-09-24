package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-09-03.
 */

public class AvgStationData {

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

        @SerializedName("station_name")
        @Expose
        private String stationName;
        @SerializedName("dateTime")
        @Expose
        private String dateTime;
        @SerializedName("avg_khaiValue")
        @Expose
        private String avgKhaiValue;
        @SerializedName("avg_pm10Value")
        @Expose
        private String avgPm10Value;
        @SerializedName("avg_pm25Value")
        @Expose
        private String avgPm25Value;
        @SerializedName("avg_no2Value")
        @Expose
        private String avgNo2Value;
        @SerializedName("avg_o3Value")
        @Expose
        private String avgO3Value;
        @SerializedName("avg_coValue")
        @Expose
        private String avgCoValue;
        @SerializedName("avg_so2Value")
        @Expose
        private String avgSo2Value;

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getAvgKhaiValue() {
            return avgKhaiValue;
        }

        public void setAvgKhaiValue(String avgKhaiValue) {
            this.avgKhaiValue = avgKhaiValue;
        }

        public String getAvgPm10Value() {
            return avgPm10Value;
        }

        public void setAvgPm10Value(String avgPm10Value) {
            this.avgPm10Value = avgPm10Value;
        }

        public String getAvgPm25Value() {
            return avgPm25Value;
        }

        public void setAvgPm25Value(String avgPm25Value) {
            this.avgPm25Value = avgPm25Value;
        }

        public String getAvgNo2Value() {
            return avgNo2Value;
        }

        public void setAvgNo2Value(String avgNo2Value) {
            this.avgNo2Value = avgNo2Value;
        }

        public String getAvgO3Value() {
            return avgO3Value;
        }

        public void setAvgO3Value(String avgO3Value) {
            this.avgO3Value = avgO3Value;
        }

        public String getAvgCoValue() {
            return avgCoValue;
        }

        public void setAvgCoValue(String avgCoValue) {
            this.avgCoValue = avgCoValue;
        }

        public String getAvgSo2Value() {
            return avgSo2Value;
        }

        public void setAvgSo2Value(String avgSo2Value) {
            this.avgSo2Value = avgSo2Value;
        }

    }
}
