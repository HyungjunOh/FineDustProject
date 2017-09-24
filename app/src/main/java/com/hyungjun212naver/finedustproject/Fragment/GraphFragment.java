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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hyungjun212naver.finedustproject.Adapter.GraphOpt2Adapter;
import com.hyungjun212naver.finedustproject.Adapter.GraphOptAdapter;
import com.hyungjun212naver.finedustproject.Bean.AvgBeaconData;
import com.hyungjun212naver.finedustproject.Bean.AvgStationData;
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

import static com.hyungjun212naver.finedustproject.App.AppConfig.LOGIN_NAME;


public class GraphFragment extends Fragment implements View.OnClickListener {

    private boolean GRAPH_OPTION_STATE = true;
    private String GRAPH_OPTION_CLICK = "station";
    private String GRAPH_DATE_SELECT = null;

    Context mContext;

    private Button graph_station_btn, graph_beacon_btn, graph_user_btn, graph_potable_search_btn;
    private RecyclerView graph_station_rV;
    private ConstraintLayout graph_opt_layout;
    private TextView graph_startDate_tV, graph_endDate_tV;

    private GraphOptAdapter graphOptAdapter;
    private GraphOpt2Adapter graphOpt2Adapter;
    private List<Station> stationList = null;
    private ArrayList<BeaconStation> beaconStationList = null;
    private List<AvgStationData.AvgAirValue> avgAirValueList = null;
    private List<AvgBeaconData.AvgAirValue> avgAirBeaconValueList = null;
    private List<AvgBeaconData.AvgAirValue> avgAirPortableValueList = null;

    String[] xValues;
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
        graph_potable_search_btn = (Button)view.findViewById(R.id.graph_potable_search_btn);
        graph_station_btn.setOnClickListener(this);
        graph_beacon_btn.setOnClickListener(this);
        graph_user_btn.setOnClickListener(this);
        graph_potable_search_btn.setOnClickListener(this);
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
                graph_potable_search_btn.setVisibility(View.GONE);
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
                graph_potable_search_btn.setVisibility(View.GONE);
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
                graph_potable_search_btn.setVisibility(View.VISIBLE);
                if(GRAPH_OPTION_STATE == false) {
                    GRAPH_OPTION_STATE = true;
                    graph_opt_layout.setVisibility(View.VISIBLE);
                } else if(GRAPH_OPTION_STATE == true && GRAPH_OPTION_CLICK == "user"){
                    GRAPH_OPTION_STATE = false;
                    graph_opt_layout.setVisibility(View.GONE);
                    graph_potable_search_btn.setVisibility(View.VISIBLE);
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
            case R.id.graph_potable_search_btn :
                Log.e("graph_potable_search", LOGIN_NAME+"/"+graph_startDate_tV.getText().toString()+"/"+graph_endDate_tV.getText().toString());
                searchPortable(LOGIN_NAME, graph_startDate_tV.getText().toString(), graph_endDate_tV.getText().toString());
                break;
        }
    }

    private void Graph_Station_Avg_AirValue(final String stationName, String start_dateTime, String end_dateTime){

        RetrofitService api = RetroClient.getStationListService();

        Call<AvgStationData> call = api.getGraph_Station_Avg_AirValue(stationName, start_dateTime, end_dateTime);

        call.enqueue(new Callback<AvgStationData>() {
            @Override
            public void onResponse(Call<AvgStationData> call, Response<AvgStationData> response) {

                if(response.isSuccessful()){

                    avgAirValueList = response.body().getAvgAirValue();

                    int count = 0;
                    int dataCount = 0;

                    for(int i=0; i<avgAirValueList.size(); i++){
                        if(avgAirValueList.get(i).getAvgKhaiValue() != null) {
                            dataCount++;
                        }
                    }

                    xValues = new String[dataCount];

                    chart.clear();

                    ArrayList<Entry> entries = new ArrayList<Entry>();

                    for(int i=0; i<avgAirValueList.size(); i++){
                        if(avgAirValueList.get(i).getAvgKhaiValue() != null) {
                            Log.e("graph", count+"------------------");
                            xValues[count] = avgAirValueList.get(i).getDateTime();
                            Log.e("graph",  "x"+ count + " : " + xValues[count]);
                            entries.add(new Entry(count, Float.parseFloat(avgAirValueList.get(i).getAvgKhaiValue())));
                            Log.e("graph", "y"+ count + " : " + avgAirValueList.get(i).getAvgKhaiValue());
                            count++;
                        }
                    }

                    LineDataSet setComp1 = new LineDataSet(entries, stationName);
                    setComp1.setCircleColors(ColorTemplate.VORDIPLOM_COLORS);
                    setComp1.setDrawCircles(true);
                    setComp1.setDrawFilled(true); //선 아래 색상 표시
                    setComp1.setDrawValues(false);

                    XAxis xAxis = chart.getXAxis();
//                    xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
                    xAxis.setGranularity(1f);

                    LineData lineData = new LineData(setComp1);
                    chart.setData(lineData);
                    chart.setScaleEnabled(false);
                    chart.invalidate();

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AvgStationData> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Graph_Beacon_Avg_AirValue(final String beacon, String start_dateTime, String end_dateTime){

        RetrofitService api = RetroClient.getStationListService();

        Call<AvgBeaconData> call = api.getGraph_Beacon_Avg_AirValue(beacon, start_dateTime, end_dateTime);

        call.enqueue(new Callback<AvgBeaconData>() {
            @Override
            public void onResponse(Call<AvgBeaconData> call, Response<AvgBeaconData> response) {

                if(response.isSuccessful()){

                    avgAirBeaconValueList = response.body().getAvgAirValue();

                    int count = 0;
                    int dataCount = 0;

                    for(int i=0; i<avgAirBeaconValueList.size(); i++){
                        if(avgAirBeaconValueList.get(i).getAvgDust() != null) {
                            dataCount++;
                        }
                    }

                    xValues = new String[dataCount];

                    chart.clear();

                    ArrayList<Entry> entries = new ArrayList<Entry>();

                    for(int i=0; i<avgAirBeaconValueList.size(); i++){
                        if(avgAirBeaconValueList.get(i).getAvgDust() != null) {
                            Log.e("graph", count+"------------------");
                            xValues[count] = avgAirBeaconValueList.get(i).getMTime();
                            Log.e("graph",  "x"+ count + " : " + xValues[count]);
                            entries.add(new Entry(count, Float.parseFloat(avgAirBeaconValueList.get(i).getAvgDust())));
                            Log.e("graph", "y"+ count + " : " + avgAirBeaconValueList.get(i).getAvgDust());
                            count++;
                        }
                    }

                    LineDataSet setComp1 = new LineDataSet(entries, beacon);
                    setComp1.setCircleColors(ColorTemplate.VORDIPLOM_COLORS);
                    setComp1.setDrawCircles(true);
                    setComp1.setDrawFilled(true); //선 아래 색상 표시
                    setComp1.setDrawValues(false);

                    XAxis xAxis = chart.getXAxis();
//                    xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
                    xAxis.setGranularity(1f);

                    LineData lineData = new LineData(setComp1);
                    chart.setData(lineData);
                    chart.setScaleEnabled(false);
                    chart.invalidate();

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AvgBeaconData> call, Throwable t) {
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

                            Graph_Station_Avg_AirValue(stationList.get(position).getStationName(), graph_startDate_tV.getText().toString(), graph_endDate_tV.getText().toString());

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

                            Graph_Beacon_Avg_AirValue(beaconStationList.get(position).getId(), graph_startDate_tV.getText().toString(), graph_endDate_tV.getText().toString());

                            Log.e("onItemClick", beaconStationList.get(position).getLocationName());
                            Log.e("onItemClick", beaconStationList.get(position).getId());

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

    private void searchPortable(final String user_id, String start_dateTime, String end_dateTime){

        RetrofitService api = RetroClient.getStationListService();

        Call<AvgBeaconData> call = api.getGraph_Portable_Avg_AirValue(user_id, start_dateTime, end_dateTime);

        call.enqueue(new Callback<AvgBeaconData>() {
            @Override
            public void onResponse(Call<AvgBeaconData> call, Response<AvgBeaconData> response) {

                if(response.isSuccessful()){

                    avgAirPortableValueList = response.body().getAvgAirValue();

                    int count = 0;
                    int dataCount = 0;

                    for(int i=0; i<avgAirPortableValueList.size(); i++){
                        if(avgAirPortableValueList.get(i).getAvgDust() != null) {
                            dataCount++;
                        }
                    }

                    xValues = new String[dataCount];

                    chart.clear();

                    ArrayList<Entry> entries = new ArrayList<Entry>();

                    for(int i=0; i<avgAirPortableValueList.size(); i++){
                        if(avgAirPortableValueList.get(i).getAvgDust() != null) {
                            Log.e("graph", count+"------------------");
                            xValues[count] = avgAirPortableValueList.get(i).getMTime();
                            Log.e("graph",  "x"+ count + " : " + xValues[count]);
                            entries.add(new Entry(count, Float.parseFloat(avgAirPortableValueList.get(i).getAvgDust())));
                            Log.e("graph", "y"+ count + " : " + avgAirPortableValueList.get(i).getAvgDust());
                            count++;
                        }
                    }

                    LineDataSet setComp1 = new LineDataSet(entries, user_id);
                    setComp1.setCircleColors(ColorTemplate.VORDIPLOM_COLORS);
                    setComp1.setDrawCircles(true);
                    setComp1.setDrawFilled(true); //선 아래 색상 표시
                    setComp1.setDrawValues(false);

                    XAxis xAxis = chart.getXAxis();
//                    xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
                    xAxis.setGranularity(1f);

                    LineData lineData = new LineData(setComp1);
                    chart.setData(lineData);
                    chart.setScaleEnabled(false);
                    chart.invalidate();

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AvgBeaconData> call, Throwable t) {
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
