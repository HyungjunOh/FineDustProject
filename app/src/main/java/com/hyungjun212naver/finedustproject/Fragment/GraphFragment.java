package com.hyungjun212naver.finedustproject.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hyungjun212naver.finedustproject.Adapter.GraphOpt2Adapter;
import com.hyungjun212naver.finedustproject.Adapter.GraphOptAdapter;
import com.hyungjun212naver.finedustproject.Bean.AirValueJSON;
import com.hyungjun212naver.finedustproject.Bean.BeaconLocation;
import com.hyungjun212naver.finedustproject.Bean.BeaconLocation.BeaconStation;
import com.hyungjun212naver.finedustproject.Bean.StationList;
import com.hyungjun212naver.finedustproject.Bean.StationList.Station;
import com.hyungjun212naver.finedustproject.R;
import com.hyungjun212naver.finedustproject.Retrofit2.RetroClient;
import com.hyungjun212naver.finedustproject.Retrofit2.RetrofitService;
import com.hyungjun212naver.finedustproject.Utility.ItemClickSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GraphFragment extends Fragment implements View.OnClickListener {

    private boolean GRAPH_OPTION_STATE = true;
    private String GRAPH_OPTION_CLICK = "station";
    private String GRAPH_DATE_SELECT = null;

    Context mContext;

    private Button graph_station_btn, graph_beacon_btn, graph_user_btn;
    private RecyclerView graph_station_rV;
    private ConstraintLayout graph_opt_layout;
    private TextView graph_startDate_tV, graph_endDate_tV;

    private GraphOptAdapter graphOptAdapter;
    private GraphOpt2Adapter graphOpt2Adapter;
    private List<Station> stationList = null;
    private ArrayList<BeaconStation> beaconStationList = null;
    private List<AirValueJSON.Airvalue> airvalueList = null;

    private LineChart chart;

    private long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    String defaultStartDate = simpleDateFormat.format(date);
    String defaultEndDate = simpleDateFormat2.format(date);

    Calendar mCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, date);
            if(GRAPH_DATE_SELECT == "graph_startDate_tV")
                updateLabel_date(graph_startDate_tV);
            else if(GRAPH_DATE_SELECT == "graph_endDate_tV")
                updateLabel_date(graph_endDate_tV);
            else
                Toast.makeText(getContext(), "날짜를 다시 선택하여 주세요.", Toast.LENGTH_SHORT).show();
        }
    };

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            // API 24 이상일 경우 시스템 기본 테마 사용
//            mContext = getContext();
//        }
        searchStation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onCreateView", "start");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        graph_opt_layout = (ConstraintLayout)view.findViewById(R.id.graph_opt_layout);
        graph_station_rV = (RecyclerView)view.findViewById(R.id.graph_station_rV);
        graph_station_btn = (Button)view.findViewById(R.id.graph_station_btn);
        graph_beacon_btn = (Button)view.findViewById(R.id.graph_beacon_btn);
        graph_user_btn = (Button)view.findViewById(R.id.graph_user_btn);
        graph_station_btn.setOnClickListener(this);
        graph_beacon_btn.setOnClickListener(this);
        graph_user_btn.setOnClickListener(this);
        graph_startDate_tV = (TextView)view.findViewById(R.id.graph_startDate_tV);
        graph_endDate_tV = (TextView)view.findViewById(R.id.graph_endDate_tV);
        graph_startDate_tV.setOnClickListener(this);
        graph_endDate_tV.setOnClickListener(this);
        graph_startDate_tV.setText(defaultStartDate + "-01");
        graph_endDate_tV.setText(defaultEndDate);

        chart = (LineChart)view.findViewById(R.id.chart);

        LinearLayoutManager hlayoutManager = new LinearLayoutManager(this.getActivity());
        hlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        graph_station_rV.setLayoutManager(hlayoutManager);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.graph_station_btn :
                graph_station_rV.setVisibility(View.VISIBLE);
                if(GRAPH_OPTION_STATE == false) {
                    GRAPH_OPTION_STATE = true;
                    graph_opt_layout.setVisibility(View.VISIBLE);
                } else if(GRAPH_OPTION_STATE == true && GRAPH_OPTION_CLICK == "station"){
                    GRAPH_OPTION_STATE = false;
                    graph_opt_layout.setVisibility(View.GONE);
                }
                searchStation();
                GRAPH_OPTION_CLICK = "station";
                break;

            case R.id.graph_beacon_btn :
                graph_station_rV.setVisibility(View.VISIBLE);
                if(GRAPH_OPTION_STATE == false) {
                    GRAPH_OPTION_STATE = true;
                    graph_opt_layout.setVisibility(View.VISIBLE);
                } else if(GRAPH_OPTION_STATE == true && GRAPH_OPTION_CLICK == "beacon"){
                    GRAPH_OPTION_STATE = false;
                    graph_opt_layout.setVisibility(View.GONE);
                }
                searchBeacon();
                GRAPH_OPTION_CLICK = "beacon";
                break;

            case R.id.graph_user_btn :
                if(GRAPH_OPTION_STATE == false) {
                    GRAPH_OPTION_STATE = true;
                    graph_opt_layout.setVisibility(View.VISIBLE);
                } else if(GRAPH_OPTION_STATE == true && GRAPH_OPTION_CLICK == "user"){
                    GRAPH_OPTION_STATE = false;
                    graph_opt_layout.setVisibility(View.GONE);
                }
                graph_station_rV.setVisibility(View.GONE);
                GRAPH_OPTION_CLICK = "user";
                break;

            case R.id.graph_startDate_tV :
                GRAPH_DATE_SELECT = "graph_startDate_tV";
                new DatePickerDialog(mContext, dateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.graph_endDate_tV :
                GRAPH_DATE_SELECT = "graph_endDate_tV";
                new DatePickerDialog(mContext, dateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void Graph_Station_AirValue(final String stationName, String start_dateTime, String end_dateTime){

        RetrofitService api = RetroClient.getStationListService();

        Call<AirValueJSON> call = api.getGraph_Station_AirValue(stationName, start_dateTime, end_dateTime);

        call.enqueue(new Callback<AirValueJSON>() {
            @Override
            public void onResponse(Call<AirValueJSON> call, Response<AirValueJSON> response) {

                if(response.isSuccessful()){

                    airvalueList = response.body().getAirvalue();

                    for(int i=0; i<airvalueList.size(); i++){
                        Log.e("graph", (i+1) + ": " + airvalueList.get(i).getKhaiValue());
                    }

                    chart.clear();

                    ArrayList<Entry> entries = new ArrayList<Entry>();
                    for(int i=0; i<airvalueList.size(); i++){
                        if(airvalueList.get(i).getKhaiValue() != null)
                            entries.add(new Entry(i+1, Float.parseFloat(airvalueList.get(i).getKhaiValue())));
                    }

                    LineDataSet setComp1 = new LineDataSet(entries, stationName);
                    setComp1.setCircleColors(ColorTemplate.VORDIPLOM_COLORS);
                    setComp1.setDrawCircles(true);
                    setComp1.setDrawFilled(true); //선 아래 색상 표시
                    setComp1.setDrawValues(false);

                    LineData lineData = new LineData(setComp1);
                    chart.setData(lineData);
                    chart.setScaleEnabled(false);
                    chart.invalidate();


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

    private void searchStation(){

        Log.e("GraphFragment", "station get!");

        RetrofitService api = RetroClient.getStationListService();

        Call<StationList> call = api.getStation();

        call.enqueue(new Callback<StationList>() {
            @Override
            public void onResponse(Call<StationList> call, Response<StationList> response) {

                if(response.isSuccessful()){

                    stationList = response.body().getStations();

                    for(int i=0; i<stationList.size(); i++){
                        Log.e("stationList", stationList.get(i).getStationName());
                    }

                    graphOptAdapter = new GraphOptAdapter(getActivity(), stationList);
                    graph_station_rV.setAdapter(graphOptAdapter);
                    ItemClickSupport.addTo(graph_station_rV).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            Graph_Station_AirValue(stationList.get(position).getStationName(), graph_startDate_tV.getText().toString()+" 00:00:00", graph_endDate_tV.getText().toString()+" 23:59:59");

                        }
                    });

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

    private void searchBeacon(){

        Log.e("GraphFragment", "beacon get!");

        RetrofitService api = RetroClient.getStationListService();

        Call<BeaconLocation> call = api.getBeacon_Location(null);

        call.enqueue(new Callback<BeaconLocation>() {
            @Override
            public void onResponse(Call<BeaconLocation> call, Response<BeaconLocation> response) {

                if(response.isSuccessful()){

                    beaconStationList = response.body().getBeaconStations();

                    graphOpt2Adapter = new GraphOpt2Adapter(getActivity(), beaconStationList);
                    graph_station_rV.setAdapter(graphOpt2Adapter);

                    ItemClickSupport.addTo(graph_station_rV).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            Log.e("onItemClick", beaconStationList.get(position).getLocationName());
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeaconLocation> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateLabel_date(TextView textView){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        textView.setText(sdf.format(mCalendar.getTime()));
    }

}
