package com.example.photos;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements album_recycler_view_interface {
    album_page_recycler_view_adapter adapter;
    ArrayList<PhotoModel> filteredPhotos = new ArrayList<>();
    User user;
    AlbumListAdapter albumListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        user = User.loadUser(getApplicationContext());

        List<Album> albums = user.getAlbums();
        RecyclerView albumList = findViewById(R.id.albumList);
        albumList.setLayoutManager(new LinearLayoutManager(this));
        albumListAdapter = new AlbumListAdapter(getApplicationContext(), albums);
        albumList.setAdapter(albumListAdapter);
        albumListAdapter.setOnDeleteAlbumClickListener(new AlbumListAdapter.deleteAlbumClickListener() {
            @Override
            public void onItemClick(int position) {
                user.deleteAlbum(position);
                user.saveUser(getApplicationContext());
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
                            user.saveUser(getApplicationContext());
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

        albumListAdapter.setOpenAlbumClickListener(new AlbumListAdapter.openAlbumClickListener() {
            @Override
            public void onAlbumClick(int position) {
                Intent intent = new Intent(HomePage.this, AlbumViewActivity.class);
                intent.putExtra("clickedAlbumPos", position);
                startActivity(intent);

                finish();
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
                            user.saveUser(getApplicationContext());
                            albumList.getAdapter().notifyItemInserted(user.getAlbums().size()-1);
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




        SearchView searchView = findViewById(R.id.photoFilterSearch);
        RecyclerView recyclerView = findViewById(R.id.filteredPhotosRecyclerView);


        adapter = new album_page_recycler_view_adapter(this, filteredPhotos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when user submits query
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
    }

    void performSearch(String query) {
//        for (Album a : user)
    }

    @Override
    public void onPhotoClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }
}


