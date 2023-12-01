package com.example.photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListViewHolder> {
    Context context;
    List<Album> albums;

    public AlbumListAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }
    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumListViewHolder(LayoutInflater.from(context).inflate(R.layout.album_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, int position) {
        holder.textView.setText(albums.get(position).albumName);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
