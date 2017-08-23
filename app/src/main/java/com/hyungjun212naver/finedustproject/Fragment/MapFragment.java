package com.hyungjun212naver.finedustproject.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.hyungjun212naver.finedustproject.App.AppConfig;
import com.hyungjun212naver.finedustproject.Bean.LatestBeaconData;
import com.hyungjun212naver.finedustproject.Bean.LatestBeaconData.BeaconData;
import com.hyungjun212naver.finedustproject.Bean.LatestStationData;
import com.hyungjun212naver.finedustproject.Bean.LatestStationData.LatestStationData_AirValue;
import com.hyungjun212naver.finedustproject.Bean.MarkerItem;
import com.hyungjun212naver.finedustproject.R;
import com.hyungjun212naver.finedustproject.Retrofit2.RetroClient;
import com.hyungjun212naver.finedustproject.Retrofit2.RetrofitService;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapFragment extends Fragment implements OnMapReadyCallback, ClusterManager.OnClusterItemClickListener, View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    private MapView mapView;

    private GoogleMap gMap;

    private View marker_root_view;
    private TextView map_marker_tV;
    private List<BeaconData> beaconDataList = null;
    private List<LatestStationData_AirValue> latestStationData_airValueList = null;

    private ClusterManager<MarkerItem> mClusterManager;

    private Button map_all_btn, map_beacon_btn, map_station_btn;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        map_all_btn = (Button)view.findViewById(R.id.map_all_btn);
        map_beacon_btn = (Button)view.findViewById(R.id.map_beacon_btn);
        map_station_btn = (Button)view.findViewById(R.id.map_station_btn);
        map_all_btn.setOnClickListener(this);
        map_beacon_btn.setOnClickListener(this);
        map_station_btn.setOnClickListener(this);

        mapView = (MapView)view.findViewById(R.id.map);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
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

    public void onClick(View v){

        switch (v.getId()){
            case R.id.map_all_btn :
                Toast.makeText(getContext(), "all_click", Toast.LENGTH_SHORT).show();
                mClusterManager.clearItems();
                mClusterManager.setRenderer(new MarkerRenderer(getContext(), gMap, mClusterManager));
                getLatestBeaconData();
                getLatestStationData();
                break;
            case R.id.map_beacon_btn :
                Toast.makeText(getContext(), "beacon_click", Toast.LENGTH_SHORT).show();
                mClusterManager.clearItems();
                mClusterManager.setRenderer(new MarkerRenderer(getContext(), gMap, mClusterManager));
                getLatestBeaconData();
                break;
            case R.id.map_station_btn :
                Toast.makeText(getContext(), "station_click", Toast.LENGTH_SHORT).show();
                mClusterManager.clearItems();
                mClusterManager.setRenderer(new MarkerRenderer(getContext(), gMap, mClusterManager));
                getLatestStationData();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;

        LatLng cPosition = new LatLng(AppConfig.cLatitude, AppConfig.cLongitude);

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(cPosition);

        markerOptions.title("현재위치");

//        markerOptions.snippet("우리집");

        gMap.addMarker(markerOptions);

        gMap.moveCamera(CameraUpdateFactory.newLatLng(cPosition));

        gMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        setCustomMarkerView();
        setUpCluster();
        getLatestBeaconData();
        getLatestStationData();
    }

    private void getLatestBeaconData(){

        RetrofitService api = RetroClient.getStationListService();

        Call<LatestBeaconData> call = api.getLatest_BeaconData();

        call.enqueue(new Callback<LatestBeaconData>() {
            @Override
            public void onResponse(Call<LatestBeaconData> call, Response<LatestBeaconData> response) {

                if(response.isSuccessful()){

                    beaconDataList = response.body().getBeaconData();

                    for(int i=0; i<beaconDataList.size(); i++){
                        Log.e("station"+(i+1)+" : ", beaconDataList.get(i).getLocationName());
                        addMarker_Beacon(beaconDataList.get(i));
                    }

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LatestBeaconData> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLatestStationData(){

        RetrofitService api = RetroClient.getStationListService();

        Call<LatestStationData> call = api.getLatestStationData_AirValue();

        call.enqueue(new Callback<LatestStationData>() {
            @Override
            public void onResponse(Call<LatestStationData> call, Response<LatestStationData> response) {

                if(response.isSuccessful()){

                    latestStationData_airValueList = response.body().getStationData();
                    for(int i=0; i<latestStationData_airValueList.size(); i++){
                        Log.e("station"+(i+1)+" : ", latestStationData_airValueList.get(i).getStationName());
                        addMarker_Station(latestStationData_airValueList.get(i));
                    }

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LatestStationData> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCustomMarkerView(){
        marker_root_view = LayoutInflater.from(this.getContext()).inflate(R.layout.custom_marker,null);
        map_marker_tV = (TextView)marker_root_view.findViewById(R.id.map_marker_tV);
    }

    private void setUpCluster(){
        mClusterManager = new ClusterManager<MarkerItem>(getContext(), gMap);
        mClusterManager.setRenderer(new MarkerRenderer(getContext(), gMap, mClusterManager));
        mClusterManager.setOnClusterItemClickListener(this);
        gMap.setOnCameraIdleListener(mClusterManager);
        gMap.setOnMarkerClickListener(mClusterManager);

    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(clusterItem.getPosition()).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        gMap.animateCamera(cameraUpdate);
        gMap.moveCamera(cameraUpdate);
        return true;
    }

    private void addMarker_Beacon(BeaconData beaconData){
        if(beaconData != null) {
            MarkerItem temp = new MarkerItem(beaconData.getLocationName(), beaconData.getLatitude(), beaconData.getLongitude(), beaconData.getDust(), beaconData.getMTime());

            mClusterManager.addItem(temp);
            mClusterManager.cluster();
        }
    }

    private void addMarker_Station(LatestStationData_AirValue la){
        if(la != null) {

            MarkerItem temp = new MarkerItem(la.getStationName(), la.getStationX(), la.getStationY(), la.getKhaiValue(), la.getDateTime());

            mClusterManager.addItem(temp);
            mClusterManager.cluster();
        }
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    private Bitmap createDrawableFromView(Context context, View view){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels,displayMetrics.heightPixels);
        view.layout(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),view.getMeasuredHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private class MarkerRenderer extends DefaultClusterRenderer<MarkerItem> {
        public MarkerRenderer(Context context, GoogleMap map, ClusterManager<MarkerItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<MarkerItem> cluster) {
            return cluster.getSize() > 1;
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerItem item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);

            LatLng position = new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
            String dust = item.getDust();
            String location = item.getLocationName();

            map_marker_tV.setText(location + "\n" + dust);

            if(dust == null) {
                map_marker_tV.setBackgroundResource(R.drawable.map_marker_purplebox);
                map_marker_tV.setTextColor(Color.WHITE);
            }
            else if(Double.parseDouble(dust) < 50){
                map_marker_tV.setBackgroundResource(R.drawable.map_marker_bluebox);
                map_marker_tV.setTextColor(Color.WHITE);
            }else if(Double.parseDouble(dust) < 150){
                map_marker_tV.setBackgroundResource(R.drawable.map_marker_greenbox);
                map_marker_tV.setTextColor(Color.WHITE);
            }else if(Double.parseDouble(dust) < 250){
                map_marker_tV.setBackgroundResource(R.drawable.map_marker_yellowbox);
                map_marker_tV.setTextColor(Color.WHITE);
            }else{
                map_marker_tV.setBackgroundResource(R.drawable.map_marker_redbox);
                map_marker_tV.setTextColor(Color.WHITE);
            }

            markerOptions.title(location);
            markerOptions.snippet(dust);
            markerOptions.position(position);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), marker_root_view)));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MarkerItem> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }
    }
}
