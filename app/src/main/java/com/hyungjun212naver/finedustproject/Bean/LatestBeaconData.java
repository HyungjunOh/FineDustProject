package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-08-23.
 */

public class LatestBeaconData {

    @SerializedName("beaconData")
    @Expose
    private List<BeaconData> beaconData = null;

    public List<BeaconData> getBeaconData() {
        return beaconData;
    }

    public void setBeaconData(List<BeaconData> beaconData) {
        this.beaconData = beaconData;
    }

    public static class BeaconData {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("dust")
        @Expose
        private String dust;
        @SerializedName("gas")
        @Expose
        private String gas;
        @SerializedName("m_time")
        @Expose
        private String mTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
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

        public String getMTime() {
            return mTime;
        }

        public void setMTime(String mTime) {
            this.mTime = mTime;
        }

    }
}