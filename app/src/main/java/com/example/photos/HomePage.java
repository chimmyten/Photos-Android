package com.example.photos;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

//        For now, creates a new User everytime
        User user = new User();

        List<Album> albums = user.getAlbums();
        RecyclerView albumList = findViewById(R.id.albumList);
        albumList.setLayoutManager(new LinearLayoutManager(this));
        AlbumListAdapter albumListAdapter = new AlbumListAdapter(getApplicationContext(), albums);
        albumList.setAdapter(albumListAdapter);
        albumListAdapter.setOnDeleteAlbumClickListener(new AlbumListAdapter.deleteAlbumClickListener() {
            @Override
            public void onItemClick(int position) {
                albums.remove(position);
                albumList.getAdapter().notifyItemRemoved(position);
            }
        });

        Button createAlbumButton = findViewById(R.id.createAlbumButton);
        createAlbumButton.setOnClickListener((view) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("Create New Album");

                final EditText albumNameEditText = new EditText(HomePage.this);
                albumNameEditText.setHint("Enter album name");
                builder.setView(albumNameEditText);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String albumName = albumNameEditText.getText().toString().trim();
                        if (!albumName.isEmpty()) {
                            // Add a new album to the list
                            Album newAlbum = new Album(albumName);
                            albums.add(newAlbum);
                            int position = albums.indexOf(newAlbum);
                            albumList.getAdapter().notifyItemInserted(position);
                            Log.d("Adapter", "Item count: " + albums.size());
                        } else {
                            Toast.makeText(HomePage.this, "Please enter a valid album name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.create().show();
        });
    }
}