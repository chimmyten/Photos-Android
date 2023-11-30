package com.example.photos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class album_page_recycler_view_adapter extends RecyclerView.Adapter<album_page_recycler_view_adapter.MyViewHolder> {
    @NonNull
    @Override
    public album_page_recycler_view_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout (give look to our rows)
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull album_page_recycler_view_adapter.MyViewHolder holder, int position) {
        //assign values to views we created in the recycler view based on postion of recycler view
    }

    @Override
    public int getItemCount() {
        //recycler view gets how many total items want to be displayed
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabs views from album_page_recycler_view_row file
        //similar to onCreate

        ImageView photo;
        TextView caption;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
