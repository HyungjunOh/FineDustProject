package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-08-20.
 */

public class AirValueJSON {

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

        @SerializedName("station_name")
        @Expose
        private String stationName;
        @SerializedName("dateTime")
        @Expose
        private String dateTime;
        @SerializedName("khaiGrade")
        @Expose
        private String khaiGrade;
        @SerializedName("khaiValue")
        @Expose
        private String khaiValue;
        @SerializedName("pm10Grade")
        @Expose
        private String pm10Grade;
        @SerializedName("pm10Value")
        @Expose
        private String pm10Value;
        @SerializedName("pm25Grade")
        @Expose
        private String pm25Grade;
        @SerializedName("pm25Value")
        @Expose
        private String pm25Value;
        @SerializedName("no2Grade")
        @Expose
        private String no2Grade;
        @SerializedName("no2Value")
        @Expose
        private String no2Value;
        @SerializedName("o3Grade")
        @Expose
        private String o3Grade;
        @SerializedName("o3Value")
        @Expose
        private String o3Value;
        @SerializedName("coGrade")
        @Expose
        private String coGrade;
        @SerializedName("coValue")
        @Expose
        private String coValue;
        @SerializedName("so2Grade")
        @Expose
        private String so2Grade;
        @SerializedName("so2Value")
        @Expose
        private String so2Value;

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

        public String getKhaiGrade() {
            return khaiGrade;
        }

        public void setKhaiGrade(String khaiGrade) {
            this.khaiGrade = khaiGrade;
        }

        public String getKhaiValue() {
            return khaiValue;
        }

        public void setKhaiValue(String khaiValue) {
            this.khaiValue = khaiValue;
        }

        public String getPm10Grade() {
            return pm10Grade;
        }

        public void setPm10Grade(String pm10Grade) {
            this.pm10Grade = pm10Grade;
        }

        public String getPm10Value() {
            return pm10Value;
        }

        public void setPm10Value(String pm10Value) {
            this.pm10Value = pm10Value;
        }

        public String getPm25Grade() {
            return pm25Grade;
        }

        public void setPm25Grade(String pm25Grade) {
            this.pm25Grade = pm25Grade;
        }

        public String getPm25Value() {
            return pm25Value;
        }

        public void setPm25Value(String pm25Value) {
            this.pm25Value = pm25Value;
        }

        public String getNo2Grade() {
            return no2Grade;
        }

        public void setNo2Grade(String no2Grade) {
            this.no2Grade = no2Grade;
        }

        public String getNo2Value() {
            return no2Value;
        }

        public void setNo2Value(String no2Value) {
            this.no2Value = no2Value;
        }

        public String getO3Grade() {
            return o3Grade;
        }

        public void setO3Grade(String o3Grade) {
            this.o3Grade = o3Grade;
        }

        public String getO3Value() {
            return o3Value;
        }

        public void setO3Value(String o3Value) {
            this.o3Value = o3Value;
        }

        public String getCoGrade() {
            return coGrade;
        }

        public void setCoGrade(String coGrade) {
            this.coGrade = coGrade;
        }

        public String getCoValue() {
            return coValue;
        }

        public void setCoValue(String coValue) {
            this.coValue = coValue;
        }

        public String getSo2Grade() {
            return so2Grade;
        }

        public void setSo2Grade(String so2Grade) {
            this.so2Grade = so2Grade;
        }

        public String getSo2Value() {
            return so2Value;
        }

        public void setSo2Value(String so2Value) {
            this.so2Value = so2Value;
        }

    }

}