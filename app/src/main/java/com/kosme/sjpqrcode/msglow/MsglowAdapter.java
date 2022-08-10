package com.kosme.sjpqrcode.msglow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kosme.sjpqrcode.R;
import com.kosme.sjpqrcode.model.Pcs;
import com.kosme.sjpqrcode.model.Serial;

import java.util.ArrayList;

public class MsglowAdapter extends RecyclerView.Adapter<MsglowAdapter.ViewHolder> {
    ArrayList<String> mList;
    Context context;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public MsglowAdapter(Context context, ArrayList<String> mList) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_msglow, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bar.setText(mList.get(position).toString());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bar = itemView.findViewById(R.id.txt_child);
        }
    }
}