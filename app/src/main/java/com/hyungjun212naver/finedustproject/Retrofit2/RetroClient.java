package com.hyungjun212naver.finedustproject.Retrofit2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hyungjun on 2017-07-13.
 */

public class RetroClient {

    private static Retrofit getRetrofitInstance(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitService getGpsToAddrService(){
        return getRetrofitInstance(RetrofitService.KAKAO_BASE_URL).create(RetrofitService.class);
    }

    public static RetrofitService getStationListService(){
        return getRetrofitInstance(RetrofitService.StationList_URL).create(RetrofitService.class);
    }


}
