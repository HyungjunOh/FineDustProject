package com.hyungjun212naver.finedustproject.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyungjun212naver.finedustproject.Adapter.BeaconSrcAdapter;
import com.hyungjun212naver.finedustproject.Bean.BeaconAirValue;
import com.hyungjun212naver.finedustproject.Bean.BeaconLocation;
import com.hyungjun212naver.finedustproject.Bean.BeaconLocation.BeaconStation;
import com.hyungjun212naver.finedustproject.R;
import com.hyungjun212naver.finedustproject.Retrofit2.RetroClient;
import com.hyungjun212naver.finedustproject.Retrofit2.RetrofitService;
import com.hyungjun212naver.finedustproject.Utility.ItemClickSupport;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BeaconSrcFragment extends Fragment {

    private ArrayList<BeaconStation> beaconStationArrayList;
    private RecyclerView beaconSrc_1_rcView;
    private BeaconSrcAdapter mAdapter;
    private EditText beaconSrc_loc_eT;
    private Button beaconSrc_loc_btn;

    private String beaconLocation_Name;

    private String id, dust, gas, time, name;

    private OnFragmentInteractionListener mListener;

    public BeaconSrcFragment() {
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
        View view = inflater.inflate(R.layout.fragment_beacon_src, container, false);

        beaconSrc_1_rcView = (RecyclerView)view.findViewById(R.id.beaconSrc_rcView);
        beaconSrc_loc_eT = (EditText)view.findViewById(R.id.beaconSrc_loc_eT);
        beaconSrc_loc_btn = (Button)view.findViewById(R.id.beaconSrc_loc_btn);

        beaconLocation_Name = null;

        initBeaconSrc();

        beaconSrc_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                beaconLocation_Name = beaconSrc_loc_eT.getText().toString();

                beaconStationArrayList = new ArrayList<>();
                mAdapter = new BeaconSrcAdapter(getActivity());
                beaconSrc_1_rcView.setAdapter(mAdapter);

                getBeaconLocationList(beaconLocation_Name);

            }
        });

        return view;
    }

    private void initBeaconSrc() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        beaconSrc_1_rcView.setLayoutManager(layoutManager);

        beaconStationArrayList = new ArrayList<>();
        mAdapter = new BeaconSrcAdapter(getActivity());
        beaconSrc_1_rcView.setAdapter(mAdapter);

        getBeaconLocationList(beaconLocation_Name);

        ItemClickSupport.addTo(beaconSrc_1_rcView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                name = beaconStationArrayList.get(position).getLocationName().toString();
                Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();

                getBeaconAirValue(beaconStationArrayList.get(position).getId());

            }
        });
    }

    private void getBeaconLocationList(String beaconLocation_Name){

        RetrofitService api = RetroClient.getStationListService();

        Call<BeaconLocation> call = api.getBeacon_Location(beaconLocation_Name);

        call.enqueue(new Callback<BeaconLocation>() {
            @Override
            public void onResponse(Call<BeaconLocation> call, Response<BeaconLocation> response) {

                if(response.isSuccessful()){

                    beaconStationArrayList = response.body().getBeaconStations();

                    mAdapter.setBeaconSrcList(beaconStationArrayList);

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

    private void getBeaconAirValue(String beacon_id){

        RetrofitService api = RetroClient.getStationListService();

        Call<BeaconAirValue> call = api.getBeacon_AirValue(beacon_id, "c");

        call.enqueue(new Callback<BeaconAirValue>() {
            @Override
            public void onResponse(Call<BeaconAirValue> call, Response<BeaconAirValue> response) {

                if(response.isSuccessful()){

                    id = response.body().getAirvalue().get(0).getId();
                    dust = response.body().getAirvalue().get(0).getDust();
                    gas = response.body().getAirvalue().get(0).getGas();
                    time = response.body().getAirvalue().get(0).getMTime();

                    BeaconSrcDialog();

                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeaconAirValue> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void BeaconSrcDialog(){

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());

        alt_bld.setMessage("측정시간 : " + time + "\n먼지값 : " + dust +
        "\n가스값 : " + gas);

        alt_bld.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                // Action for 'Yes' Button
            }
        });

        alt_bld.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                        // Action for 'NO' Button
                dialog.cancel();
            }
        });

        alt_bld.setNeutralButton("자세히 보기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        });

        // Title for AlertDialog
        alt_bld.setTitle(name + "의 측정값");

        AlertDialog alertDialog = alt_bld.create();

        alertDialog.show();
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
