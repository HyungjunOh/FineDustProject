package com.hyungjun212naver.finedustproject.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyungjun212naver.finedustproject.Activity.MainActivity;
import com.hyungjun212naver.finedustproject.App.AppConfig;
import com.hyungjun212naver.finedustproject.Bean.AirValueJSON;
import com.hyungjun212naver.finedustproject.Bean.BeaconSrcToId;
import com.hyungjun212naver.finedustproject.Bean.GpsToAddr;
import com.hyungjun212naver.finedustproject.Bean.InsertBeaconData;
import com.hyungjun212naver.finedustproject.Bean.StationList;
import com.hyungjun212naver.finedustproject.R;
import com.hyungjun212naver.finedustproject.Retrofit2.RetroClient;
import com.hyungjun212naver.finedustproject.Retrofit2.RetrofitService;
import com.hyungjun212naver.finedustproject.Utility.GpsInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hyungjun212naver.finedustproject.App.AppConfig.BEACON_STATE;
import static com.hyungjun212naver.finedustproject.Utility.Utility.getBlinkAnimation;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private GpsInfo gps;
    double latitude;
    double longitude;
    String address;
    String station_name;
    String Beacon_Station_name;

    String khaiGrade, khaiValue, pm10Grade, pm10Value, pm25Grade, pm25Value,
            no2Grade, no2Value, o3Grade, o3Value, coGrade, coValue, so2Grade, so2Value;

    private FrameLayout home_frameLayout;
    private ConstraintLayout layout;
    private ScrollView home_background;
    private SwipeRefreshLayout home_swipeLayout;
    private TextView home_tV_cLocation, home_tV_dLocation, home_tV_dustLevel, home_tV_dustValue, home_tV_etcAirValue;
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

        home_frameLayout = (FrameLayout)view.findViewById(R.id.home_frameLayout);
        home_background = (ScrollView)view.findViewById(R.id.home_background);
        home_tV_cLocation = (TextView)view.findViewById(R.id.home_tV_cLocation);
        home_tV_dLocation = (TextView)view.findViewById(R.id.home_tV_dLocation);
        home_tV_dustLevel = (TextView)view.findViewById(R.id.home_tV_dustLevel);
        home_tV_dustValue = (TextView)view.findViewById(R.id.home_tV_dustValue);
        home_tV_etcAirValue = (TextView)view.findViewById(R.id.home_tV_etcAirValue);
        home_iV_faceState = (ImageView) view.findViewById(R.id.home_iV_faceState);

        home_swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.home_swipeLayout);
        home_swipeLayout.setOnRefreshListener(this);

        if (BEACON_STATE == false) {
            //현재 GPS 수신.
            getGps();
        } else {
            setBeaconData();
        }

        return view;
    }

    @Override
    public void onRefresh() {

        if(BEACON_STATE == false){
            getGps();
        } else {
            setBeaconData();
        }

        home_swipeLayout.setRefreshing(false);
    }

    private void getGps(){

        gps = new GpsInfo(getContext());

        if (gps.isGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            AppConfig.cLatitude = gps.getLatitude();
            AppConfig.cLongitude = gps.getLongitude();

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

                    home_tV_cLocation.setText("현재 장소 : " + address);

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

        Call<StationList> call = api.getStation_Distance(String.valueOf(latitude), String.valueOf(longitude));

        call.enqueue(new Callback<StationList>() {
            @Override
            public void onResponse(Call<StationList> call, Response<StationList> response) {

                if(response.isSuccessful()){

                    station_name = response.body().getStations().get(0).getStationName();

                    home_tV_dLocation.setText("측정장소 : " + station_name);

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

                    String khaiGradetoString = null;

                    if(khaiGrade.equals("1")) {
                        khaiGradetoString = "좋음";
                        home_background.setBackgroundResource(R.color.base_blue);
                        home_iV_faceState.setImageResource(R.drawable.faceimg_verygood);
                    }
                    else if(khaiGrade.equals("2")) {
                        khaiGradetoString = "보통";
                        home_background.setBackgroundResource(R.color.base_green);
                        home_iV_faceState.setImageResource(R.drawable.faceimg_nomal);

                    }
                    else if(khaiGrade.equals("3")) {
                        khaiGradetoString = "나쁨";
                        home_background.setBackgroundResource(R.color.base_yellow);
                        home_iV_faceState.setImageResource(R.drawable.faceimg_worse);
                        blick();
                    }
                    else if(khaiGrade.equals("4")) {
                        khaiGradetoString = "매우나쁨";
                        home_background.setBackgroundResource(R.color.base_red);
                        home_iV_faceState.setImageResource(R.drawable.faceimg_veryworse);
                        blick();
                    }
                    else {
                        khaiGradetoString = "미측정";
                        home_background.setBackgroundResource(R.color.base_background);
                        home_iV_faceState.setImageResource(R.drawable.faceimg_undefine);
                    }

                    home_tV_dustLevel.setText(khaiGradetoString);
                    home_tV_dustValue.setText("미세먼지지수 : " + khaiValue);

                    home_tV_etcAirValue.setText("pm10 : " + pm10Value + "\npm25 : " + pm25Value + "\nno2 : " + no2Value + "\no3 : " + o3Value + "\nco : " + coValue + "\nso2 : " + so2Value);

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

    private void searchBeaconStation(String id){

        RetrofitService api = RetroClient.getStationListService();

        Call<BeaconSrcToId> call = api.getBeacon_Location_Id(id);

        call.enqueue(new Callback<BeaconSrcToId>() {
            @Override
            public void onResponse(Call<BeaconSrcToId> call, Response<BeaconSrcToId> response) {

                if(response.isSuccessful()){

                    Beacon_Station_name = response.body().getBeacon().get(0).getLocationName();

                    home_tV_cLocation.setText("현재 장소 : " + Beacon_Station_name);

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeaconSrcToId> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBeaconData(){

        String ibe3 = MainActivity.UUID.substring(28);
        int beacon_station_id = Integer.parseInt(ibe3, 16);

        home_tV_dLocation.setText(MainActivity.UUID+"\n"+
                MainActivity.UUID.substring(28)+"=>"+String.valueOf(beacon_station_id));

        searchBeaconStation(String.valueOf(beacon_station_id));

        String khaiGradetoString = "NULL";

        int gasValue = Integer.parseInt(MainActivity.Major.substring(0, 1), 16);
        int dustValue = Integer.parseInt(MainActivity.Major.substring(1), 16);

        if(dustValue < 50) {
            khaiGradetoString = "좋음";
            home_background.setBackgroundResource(R.color.base_blue);
            home_iV_faceState.setImageResource(R.drawable.faceimg_verygood);
            blick();
        }
        else if(dustValue < 100) {
            khaiGradetoString = "보통";
            home_background.setBackgroundResource(R.color.base_green);
            home_iV_faceState.setImageResource(R.drawable.faceimg_nomal);

        }
        else if(dustValue < 200) {
            khaiGradetoString = "나쁨";
            home_background.setBackgroundResource(R.color.base_yellow);
            home_iV_faceState.setImageResource(R.drawable.faceimg_worse);

        }
        else if(dustValue < 700) {
            khaiGradetoString = "매우나쁨";
            home_background.setBackgroundResource(R.color.base_red);
            home_iV_faceState.setImageResource(R.drawable.faceimg_veryworse);
        }
        else {
            khaiGradetoString = "미측정//오류";
            home_background.setBackgroundResource(R.color.base_background);
            home_iV_faceState.setImageResource(R.drawable.faceimg_undefine);
        }

        home_tV_dustLevel.setText(khaiGradetoString);
        home_tV_dustValue.setText("미세먼지지수 : " + dustValue);

        home_tV_etcAirValue.setText("가스 농도 : "+gasValue);

        insertBeaconData(String.valueOf(beacon_station_id), String.valueOf(dustValue), String.valueOf(gasValue));

    }

    private void insertBeaconData(String id, String dust, String gas){

        RetrofitService api = RetroClient.getStationListService();

        Call<InsertBeaconData> call = api.insert_Beacon_Data(id, dust, gas);

        call.enqueue(new Callback<InsertBeaconData>() {
            @Override
            public void onResponse(Call<InsertBeaconData> call, Response<InsertBeaconData> response) {

                if(response.isSuccessful()){

                    String result = response.body().getResult();

                    if(result.equals("success"))
                        Toast.makeText(getContext(), "비콘 데이터 저장", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "비콘 데이터 저장 실패", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertBeaconData> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void blick(){

        home_frameLayout.removeView(layout);
        layout = new ConstraintLayout(getContext());
        layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setBackgroundColor(Color.RED);
//                    layout.setAlpha(0.5f);
        home_frameLayout.addView(layout);
        layout.startAnimation(getBlinkAnimation());

        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        layout.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                home_frameLayout.removeView(layout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

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
