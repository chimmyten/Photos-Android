package com.example.photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class album_page_recycler_view_adapter extends RecyclerView.Adapter<album_page_recycler_view_adapter.MyViewHolder> {
    Context context;
    ArrayList<PhotoModel> photoModels;
    public album_page_recycler_view_adapter(Context context, ArrayList<PhotoModel> photoModels){
        this.photoModels = photoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public album_page_recycler_view_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout (give look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.album_page_recycler_view_row, parent, false);
        return new album_page_recycler_view_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull album_page_recycler_view_adapter.MyViewHolder holder, int position) {
        //assign values to views we created in the recycler view based on postion of recycler view
        holder.photo.setImageResource(photoModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        //recycler view gets how many total items want to be displayed
        return photoModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabs views from album_page_recycler_view_row file
        //similar to onCreate

        ImageView photo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView);

        }
    }

}
