package com.example.photos;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumListViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    public AlbumListViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.albumListItemName);
    }
}
