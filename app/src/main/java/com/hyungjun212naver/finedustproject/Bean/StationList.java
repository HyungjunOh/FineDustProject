package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-08-18.
 */

public class StationList {

    @SerializedName("stations")
    @Expose
    private List<Station> stations = null;

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public class Station {

        @SerializedName("station_code")
        @Expose
        private String stationCode;
        @SerializedName("station_name")
        @Expose
        private String stationName;
        @SerializedName("station_x")
        @Expose
        private String stationX;
        @SerializedName("station_y")
        @Expose
        private String stationY;

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public String getStationX() {
            return stationX;
        }

        public void setStationX(String stationX) {
            this.stationX = stationX;
        }

        public String getStationY() {
            return stationY;
        }

        public void setStationY(String stationY) {
            this.stationY = stationY;
        }

    }

}
