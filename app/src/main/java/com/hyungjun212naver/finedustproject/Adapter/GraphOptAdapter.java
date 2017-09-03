package com.hyungjun212naver.finedustproject.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyungjun212naver.finedustproject.Bean.StationList;
import com.hyungjun212naver.finedustproject.R;

import java.util.List;

/**
 * Created by hyung on 2017-08-25.
 */

public class GraphOptAdapter extends RecyclerView.Adapter<GraphOptAdapter.GraphOptViewHolder>{

    private Context context;
    private List<StationList.Station> arrayList;

    public GraphOptAdapter(Context context, List<StationList.Station> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public GraphOptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_opt_item, parent, false);
        GraphOptViewHolder holder = new GraphOptViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GraphOptViewHolder holder, int position) {

        holder.textView.setText(arrayList.get(position).getStationName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class GraphOptViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public GraphOptViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.graph_opt_item_tV);
        }
    }
}
