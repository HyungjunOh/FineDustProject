package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-09-05.
 */

public class BeaconSrcToId {

    @SerializedName("beacon")
    @Expose
    private List<Beacon> beacon = null;

    public List<Beacon> getBeacon() {
        return beacon;
    }

    public void setBeacon(List<Beacon> beacon) {
        this.beacon = beacon;
    }

    public class Beacon {

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

    }
}
