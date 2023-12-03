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
                user.deleteAlbum(position);
                albumList.getAdapter().notifyItemRemoved(position);
            }
        });

        albumListAdapter.setOnRenameAlbumClickListener(new AlbumListAdapter.renameAlbumClickListener() {
            @Override
            public void onRenameButtonClick(int position) {
//                set up renaming logic
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("Rename Album");

                final EditText albumNameEditText = new EditText(HomePage.this);
                albumNameEditText.setHint("Enter new album name");
                builder.setView(albumNameEditText);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String albumName = albumNameEditText.getText().toString().trim();
                        if (!albumName.isEmpty()) {
                            for (Album album : user.getAlbums()) {
                                if (album.getAlbumName().equals(albumName)) {
                                    Toast.makeText(HomePage.this, "Album name already used", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            // Rename the album
                            user.renameAlbum(position, albumName);
                            albumList.getAdapter().notifyItemChanged(position);
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
                            for (Album album : user.getAlbums()) {
                                if (album.getAlbumName().equals(albumName)) {
                                    Toast.makeText(HomePage.this, "Album name already used", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            // Add a new album to the list
                            Album newAlbum = new Album(albumName);
                            user.addAlbum(newAlbum);
                            albumList.getAdapter().notifyItemInserted(user.getAlbums().size()-1);
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