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
import com.kosme.sjpqrcode.model.Pcs;

import java.util.ArrayList;

public class ExpiredAdapter extends RecyclerView.Adapter<ExpiredAdapter.ViewHolder>{
    ArrayList<Pcs> mList;
    Context context;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public ExpiredAdapter(Context context, ArrayList<Pcs> mList) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ExpiredAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_ed, parent, false);
        return new ExpiredAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ExpiredAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ed.setText(mList.get(position).getEd().replace("00:00:00", ""));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ed = itemView.findViewById(R.id.txt_ed);
        }
    }
}
