package com.hyungjun212naver.finedustproject.Bean;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by hyung on 2017-08-24.
 */

public class MarkerItem implements ClusterItem{

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
    @SerializedName("m_time")
    @Expose
    private String mTime;

    public MarkerItem(String locationName, String latitude, String longitude, String dust, String mTime) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dust = dust;
        this.mTime = mTime;
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

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
