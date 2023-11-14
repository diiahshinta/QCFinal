package com.kosme.sjpqrcode.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kosme.sjpqrcode.R;
import com.kosme.sjpqrcode.model.DataSerialisasi;
import com.kosme.sjpqrcode.model.DetailsSerialisasi;

import java.util.ArrayList;

public class SerialisasiAdapter extends RecyclerView.Adapter<SerialisasiAdapter.ViewHolder>{
    ArrayList<DataSerialisasi> mList;
    Context context;
    RecyclerView rv;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public SerialisasiAdapter(Context context, ArrayList<DataSerialisasi> mList) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public SerialisasiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_serialisasi, parent, false);
        return new SerialisasiAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(SerialisasiAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.serialisasi.setText(mList.get(position).getBarcode());
        DuplicateAdapter duplicateAdapter = new DuplicateAdapter(context, mList.get(position).getDetailsSerialisasi());
        holder.recyclerView.setAdapter(duplicateAdapter);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView serialisasi;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serialisasi = itemView.findViewById(R.id.serialisasi);
            recyclerView = itemView.findViewById(R.id.rv_duplicate);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
