package com.example.photos;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class album_page_recycler_view_adapter extends RecyclerView.Adapter<album_page_recycler_view_adapter.MyViewHolder> {
    Context context;
    ArrayList<PhotoModel> photoModels;

    private final album_recycler_view_interface albumRecyclerViewInterface;
    public album_page_recycler_view_adapter(Context context, ArrayList<PhotoModel> photoModels, album_recycler_view_interface albumRecyclerViewInterface){
        this.photoModels = photoModels;
        this.context = context;
        this.albumRecyclerViewInterface = albumRecyclerViewInterface;
    }

    @NonNull
    @Override
    public album_page_recycler_view_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout (give look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.album_page_recycler_view_row, parent, false);
        return new album_page_recycler_view_adapter.MyViewHolder(view, albumRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull album_page_recycler_view_adapter.MyViewHolder holder, int position) {
        //assign values to views we created in the recycler view based on position of recycler view
        holder.photo.setImageURI(Uri.parse(photoModels.get(position).getPhotoURI()));
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
        ImageView deleteButton;
        public MyViewHolder(@NonNull View itemView, album_recycler_view_interface albumRecyclerViewInterface) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(view -> {
                if (albumRecyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos!=RecyclerView.NO_POSITION){
                        albumRecyclerViewInterface.onPhotoClick(pos);
                    }
                }
            });

            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(view -> {
                if (albumRecyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos!=RecyclerView.NO_POSITION){
                        albumRecyclerViewInterface.onDeleteClick(pos);
                    }
                }
            });

        }
    }


    public void notifyOfAdd() {
        notifyItemInserted(photoModels.size() - 1); // Notify adapter about the data change
    }

    public void notifyOfRemoval(int pos) {
        notifyItemRemoved(pos);
    }

}
