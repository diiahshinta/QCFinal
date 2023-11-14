package com.kosme.sjpqrcode.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kosme.sjpqrcode.R;
import com.kosme.sjpqrcode.model.DetailsSerialisasi;
import com.kosme.sjpqrcode.model.Pcs;

import java.util.ArrayList;

public class DuplicateAdapter extends RecyclerView.Adapter<DuplicateAdapter.ViewHolder>{
    ArrayList<DetailsSerialisasi> mList;
    Context context;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public DuplicateAdapter(Context context, ArrayList<DetailsSerialisasi> mList) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public DuplicateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_duplicate, parent, false);
        return new DuplicateAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(DuplicateAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.palet.setText(mList.get(position).getPallete());
        holder.box.setText(mList.get(position).getBox());
        holder.inner.setText(mList.get(position).getInner());
        holder.pcs.setText(mList.get(position).getPcs());
        holder.scanned.setText(mList.get(position).getScanned());
        holder.station.setText(mList.get(position).getStationName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView palet, box, inner, pcs, scanned, station;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            palet = itemView.findViewById(R.id.txt_palet);
            box = itemView.findViewById(R.id.txt_box);
            inner = itemView.findViewById(R.id.txt_inner);
            pcs = itemView.findViewById(R.id.txt_pcs);
            scanned = itemView.findViewById(R.id.txt_scanned);
            station = itemView.findViewById(R.id.txt_station_name);
        }
    }
}
