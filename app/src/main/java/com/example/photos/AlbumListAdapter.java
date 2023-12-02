package com.example.photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListViewHolder> {
    Context context;
    List<Album> albums;
    deleteAlbumClickListener deleteAlbumListener;

    public interface deleteAlbumClickListener {
        void onItemClick(int position);
    }

    public void setOnDeleteAlbumClickListener(deleteAlbumClickListener clickListener) {
        deleteAlbumListener = clickListener;
    }
    public AlbumListAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }
    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.album_list_item_view, parent, false);
        return new AlbumListViewHolder(v, deleteAlbumListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, int position) {
        holder.textView.setText(albums.get(position).getAlbumName());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
