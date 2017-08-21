package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hyung on 2017-08-21.
 */

public class BeaconLocation {

    @SerializedName("beacon_stations")
    @Expose
    private ArrayList<BeaconStation> beaconStations = null;

    public ArrayList<BeaconStation> getBeaconStations() {
        return beaconStations;
    }

    public void setBeaconStations(ArrayList<BeaconStation> beaconStations) {
        this.beaconStations = beaconStations;
    }

    public class BeaconStation {

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
