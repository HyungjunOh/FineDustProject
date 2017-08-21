//package com.hyungjun212naver.finedustproject.Retrofit2;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by hyungjun on 2017-07-13.
// */
//
//public class RestManager {
//
//    private RetrofitService mItemService;
//
//    public RetrofitService getmItemService(){
//        if(mItemService == null){
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Constants.HTTP.KAKAO_BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            mItemService = retrofit.create(RetrofitService.class);
//        }
//
//        return mItemService;
//    }
//}
