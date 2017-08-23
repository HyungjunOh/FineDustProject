package com.hyungjun212naver.finedustproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyungjun212naver.finedustproject.Bean.BeaconLocation.BeaconStation;
import com.hyungjun212naver.finedustproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyungjun on 2017-07-13.
 */

public class BeaconSrcAdapter extends RecyclerView.Adapter<BeaconSrcAdapter.Holder> {

    List<BeaconStation> beaconStationList;
    Context context;
    LinearLayoutManager mLinearLayoutManager;

    public BeaconSrcAdapter(Activity context){
        beaconStationList = new ArrayList<>();
        context = context;
    }

    public BeaconSrcAdapter(List<BeaconStation> beaconStationList) {
        this.beaconStationList = beaconStationList;
    }

    public void setBeaconSrcList(List<BeaconStation> beaconStations){
        beaconStationList.clear();
        beaconStationList.addAll(beaconStations);
        this.notifyItemRangeInserted(0, beaconStations.size());
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setRecycleView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.beaconsrc_list_item, parent, false);

        Holder vh = new Holder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BeaconSrcAdapter.Holder holder, int position) {
        BeaconStation item = beaconStationList.get(position);

        holder.beaconsrc_id.setText(item.getId());
        holder.beaconsrc_name.setText(item.getLocationName());

    }

    @Override
    public int getItemCount() {
        return beaconStationList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        public TextView beaconsrc_id;
        public TextView beaconsrc_name;

        public Holder(View itemView) {
            super(itemView);
            beaconsrc_id = (TextView) itemView.findViewById(R.id.beaconsrc_id);
            beaconsrc_name = (TextView) itemView.findViewById(R.id.beaconsrc_name);
        }

    }

    public void add(BeaconStation data){
        beaconStationList.add(data);
        notifyDataSetChanged();
    }

}

