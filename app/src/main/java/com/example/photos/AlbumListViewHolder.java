package com.example.photos;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumListViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView imageView;
    Button button;
    public AlbumListViewHolder(@NonNull View itemView, AlbumListAdapter.deleteAlbumClickListener deleteAlbumListener) {
        super(itemView);
        textView = itemView.findViewById(R.id.albumListItemName);
        imageView = itemView.findViewById(R.id.deleteAlbum);
        button = itemView.findViewById(R.id.renameAlbumButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                deleteAlbumListener.onItemClick(getAdapterPosition());
            }
        });
    }
}
