package com.hyungjun212naver.finedustproject.Retrofit2;

import com.hyungjun212naver.finedustproject.App.AppConfig;
import com.hyungjun212naver.finedustproject.Bean.AirValueJSON;
import com.hyungjun212naver.finedustproject.Bean.BeaconAirValue;
import com.hyungjun212naver.finedustproject.Bean.BeaconLocation;
import com.hyungjun212naver.finedustproject.Bean.GpsToAddr;
import com.hyungjun212naver.finedustproject.Bean.LatestBeaconData;
import com.hyungjun212naver.finedustproject.Bean.LatestStationData;
import com.hyungjun212naver.finedustproject.Bean.StationList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by hyungjun on 2017-07-13.
 */

public interface RetrofitService {

    public static final String KAKAO_BASE_URL = "https://dapi.kakao.com";
    public static final String StationList_URL = "http://" + AppConfig.IP;

    @Headers("Authorization:KakaoAK c7864224d7969af2d81c8b202beeda0d")
    @GET("/v2/local/geo/coord2regioncode.json")
    Call<GpsToAddr> getAddr(@Query("x") String x, @Query("y") String y);

    @GET("/FineDustProject/stationlist.php")
    Call<StationList> getStation();

    @GET("/FineDustProject/stationlist2.php")
    Call<StationList> getStation_Distance(@Query("latitude") String latitude, @Query("longitude") String longitude);

    @GET("/FineDustProject/latest_airvalue.php")
    Call<AirValueJSON> getLatest_AirValue(@Query("stationName") String stationName);

    @GET("/FineDustProject/beacon_location.php")
    Call<BeaconLocation> getBeacon_Location(@Query("beaconLocation_Name") String beaconLocation_Name);

    @GET("/FineDustProject/beacon_data.php")
    Call<BeaconAirValue> getBeacon_AirValue(@Query("beacon_id") String beacon_id, @Query("count") String count);

    @GET("/FineDustProject/latest_beacon_airvalue.php")
    Call<LatestBeaconData> getLatest_BeaconData();

    @GET("/FineDustProject/latest_station_airvalue.php")
    Call<LatestStationData> getLatestStationData_AirValue();

    @GET("/FineDustProject/graph_station_airvalue.php?")
    Call<AirValueJSON> getGraph_Station_AirValue(@Query("stationName") String stationName, @Query("start_dateTime") String start_dateTime,
                                                 @Query("end_dateTime") String end_dateTime);
}