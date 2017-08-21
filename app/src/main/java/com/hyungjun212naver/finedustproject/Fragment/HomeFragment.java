package com.hyungjun212naver.finedustproject.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyungjun212naver.finedustproject.Bean.AirValueJSON;
import com.hyungjun212naver.finedustproject.Bean.GpsToAddr;
import com.hyungjun212naver.finedustproject.Bean.StationList;
import com.hyungjun212naver.finedustproject.R;
import com.hyungjun212naver.finedustproject.Retrofit2.RetrofitService;
import com.hyungjun212naver.finedustproject.Retrofit2.RetroClient;
import com.hyungjun212naver.finedustproject.Utility.GpsInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private GpsInfo gps;
    double latitude;
    double longitude;
    String address;
    String station_name;

    String khaiGrade, khaiValue, pm10Grade, pm10Value, pm25Grade, pm25Value,
            no2Grade, no2Value, o3Grade, o3Value, coGrade, coValue, so2Grade, so2Value;

    private TextView home_tV_cLocation, home_tV_dLocation, home_tV_airValue;
    private ImageView home_iV_faceState;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        home_tV_cLocation = (TextView)view.findViewById(R.id.home_tV_cLocation);
        home_tV_dLocation = (TextView)view.findViewById(R.id.home_tV_dLocation);
        home_tV_airValue = (TextView)view.findViewById(R.id.home_tV_airValue);
        home_iV_faceState = (ImageView) view.findViewById(R.id.home_iV_faceState);

        //현재 GPS 수신.
        getGps();



        return view;
    }

    private void getGps(){

        gps = new GpsInfo(getContext());

        if (gps.isGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.e("HomeFragment Tag", "현재 GPS = latitude : " + latitude + " // longitude : " + longitude );

            //주소 변환 - KAKAO RESTful API
            convertAddr();

            //주소의 측정소 찾기 - MYSQL 저장 측정소 거리 비교하여 가까운 곳 return
            searchStation();



        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }
    }

    private void convertAddr() {

        RetrofitService api = RetroClient.getGpsToAddrService();

        Call<GpsToAddr> call = api.getAddr(String.valueOf(longitude), String.valueOf(latitude));

        call.enqueue(new Callback<GpsToAddr>() {
            @Override
            public void onResponse(Call<GpsToAddr> call, Response<GpsToAddr> response) {

                if(response.isSuccessful()){

                    address = response.body().getDocuments().get(1).getRegion3depthName();

                    home_tV_cLocation.append(address);

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GpsToAddr> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchStation(){

        RetrofitService api = RetroClient.getStationListService();

        Call<StationList> call = api.getStation(String.valueOf(latitude), String.valueOf(longitude));

        call.enqueue(new Callback<StationList>() {
            @Override
            public void onResponse(Call<StationList> call, Response<StationList> response) {

                if(response.isSuccessful()){

                    station_name = response.body().getStations().get(0).getStationName();

                    home_tV_dLocation.append(station_name);

                    //측정소의 최근 값 찾기
                    latestAirValue(station_name);

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StationList> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void latestAirValue(String stationName){

        RetrofitService api = RetroClient.getStationListService();

        Call<AirValueJSON> call = api.getLatest_AirValue(stationName);

        call.enqueue(new Callback<AirValueJSON>() {
            @Override
            public void onResponse(Call<AirValueJSON> call, Response<AirValueJSON> response) {

                if(response.isSuccessful()){

                    station_name = response.body().getAirvalue().get(0).getStationName();

                    khaiGrade = response.body().getAirvalue().get(0).getKhaiGrade();
                    khaiValue = response.body().getAirvalue().get(0).getKhaiValue();
                    pm10Grade = response.body().getAirvalue().get(0).getPm10Grade();
                    pm10Value = response.body().getAirvalue().get(0).getPm10Value();
                    pm25Grade = response.body().getAirvalue().get(0).getPm25Grade();
                    pm25Value = response.body().getAirvalue().get(0).getPm25Value();
                    no2Grade = response.body().getAirvalue().get(0).getNo2Grade();
                    no2Value = response.body().getAirvalue().get(0).getNo2Value();
                    o3Grade = response.body().getAirvalue().get(0).getO3Grade();
                    o3Value = response.body().getAirvalue().get(0).getO3Value();
                    coGrade = response.body().getAirvalue().get(0).getCoGrade();
                    coValue = response.body().getAirvalue().get(0).getCoValue();
                    so2Grade = response.body().getAirvalue().get(0).getSo2Grade();
                    so2Value = response.body().getAirvalue().get(0).getSo2Value();

                    home_tV_airValue.append(khaiGrade + " / " + khaiValue);

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AirValueJSON> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
