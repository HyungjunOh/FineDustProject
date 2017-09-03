package com.hyungjun212naver.finedustproject.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyungjun212naver.finedustproject.Bean.BeaconLocation;
import com.hyungjun212naver.finedustproject.R;

import java.util.ArrayList;

/**
 * Created by hyung on 2017-08-25.
 */

public class GraphOpt2Adapter extends RecyclerView.Adapter<GraphOpt2Adapter.GraphOpt2ViewHolder>{

    private Context context;
    private ArrayList<BeaconLocation.BeaconStation> arrayList;

    public GraphOpt2Adapter(Context context, ArrayList<BeaconLocation.BeaconStation> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public GraphOpt2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_opt_item, parent, false);
        GraphOpt2ViewHolder holder = new GraphOpt2ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GraphOpt2ViewHolder holder, int position) {

        holder.textView.setText(arrayList.get(position).getLocationName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class GraphOpt2ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public GraphOpt2ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.graph_opt_item_tV);
        }
    }
}
