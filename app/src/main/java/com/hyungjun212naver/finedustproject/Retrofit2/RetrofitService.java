package com.hyungjun212naver.finedustproject.Retrofit2;

import com.hyungjun212naver.finedustproject.App.AppConfig;
import com.hyungjun212naver.finedustproject.Bean.AirValueJSON;
import com.hyungjun212naver.finedustproject.Bean.GpsToAddr;
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

    @GET("/FineDustProject/stationlist2.php")
    Call<StationList> getStation(@Query("latitude") String latitude, @Query("longitude") String longitude);

    @GET("/FineDustProject/latest_airvalue.php")
    Call<AirValueJSON> getLatest_AirValue(@Query("stationName") String stationName);
}