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
    album_page_recycler_view_adapter adapterFilteredPhotos;
    ArrayList<PhotoModel> filteredPhotos = new ArrayList<>();
    User user;
    AlbumListAdapter albumListAdapter;

    boolean canSwitchToAlbums = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        user = User.loadUser(getApplicationContext());

        List<Album> albums = user.getAlbums();
        RecyclerView recyclerView = findViewById(R.id.albumList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumListAdapter = new AlbumListAdapter(getApplicationContext(), albums);
        recyclerView.setAdapter(albumListAdapter);
        albumListAdapter.setOnDeleteAlbumClickListener(new AlbumListAdapter.deleteAlbumClickListener() {
            @Override
            public void onItemClick(int position) {
                user.deleteAlbum(position);
                user.saveUser(getApplicationContext());
                recyclerView.getAdapter().notifyItemRemoved(position);
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
                            recyclerView.getAdapter().notifyItemChanged(position);
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
                            recyclerView.getAdapter().notifyItemInserted(user.getAlbums().size()-1);
                        } else {
                            Toast.makeText(HomePage.this, "Please enter a valid album name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

                builder.create().show();
        });


        Button switchToAlbumBtn = findViewById(R.id.switchButton);


        SearchView searchView = findViewById(R.id.photoFilterSearch);
        adapterFilteredPhotos = new album_page_recycler_view_adapter(getApplicationContext(), filteredPhotos, this);

        switchToAlbumBtn.setOnClickListener(view -> {
            if (canSwitchToAlbums){
                recyclerView.setAdapter(albumListAdapter);
                canSwitchToAlbums = false;
                searchView.setIconified(true);
                // Optionally, clear the query text
                searchView.setQuery("", false);
            } else {
                Toast.makeText(getApplicationContext(), "Already viewing albums!", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnSearchClickListener(v -> {
            if (!canSwitchToAlbums){
                canSwitchToAlbums = true;
                filteredPhotos.clear();
                for (Album a : user.getAlbums()){
                    for (PhotoModel p : a.getPhotoModelsArrayList()){
                        filteredPhotos.add(p);
                    }
                }
                recyclerView.setAdapter(adapterFilteredPhotos);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when user submits query
                searchView.clearFocus(); // Clear focus from the SearchView
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
        filteredPhotos.clear();
        adapterFilteredPhotos.notifyDataSetChanged();
            String[] newQueryAnd = query.split(" and ");
            String[] newQueryOr = query.split(" or ");

        if (newQueryAnd.length>1){
                    for (Album a : user.getAlbums()){
                        for (PhotoModel p : a.getPhotoModelsArrayList()){
                            boolean firstQuery = false;
                            boolean secondQuery = false;
                            for (String tag : p.getTagList()){
                                String toLower = tag.toLowerCase();
                                String qToLow2 = newQueryAnd[1].toLowerCase();

                                if (tag.equalsIgnoreCase(newQueryAnd[0])) firstQuery = true;
                                if (toLower.startsWith(qToLow2)) secondQuery = true;
                            }
                            if (firstQuery&&secondQuery) {
                                filteredPhotos.add(p);
                                adapterFilteredPhotos.notifyOfAdd();
                            }
                        }
                    }
            } else if(newQueryOr.length>1){

                for (Album a : user.getAlbums()){
                for (PhotoModel p : a.getPhotoModelsArrayList()){
                    boolean firstQuery = false;
                    boolean secondQuery = false;
                    for (String tag : p.getTagList()){
                        String toLower = tag.toLowerCase();
                        String qToLow2 = newQueryOr[1].toLowerCase();
                        if (tag.equalsIgnoreCase(newQueryOr[0])) firstQuery = true;
                        if (toLower.startsWith(qToLow2)) secondQuery = true;
                    }
                    if (firstQuery||secondQuery){
                        filteredPhotos.add(p);
                        adapterFilteredPhotos.notifyOfAdd();
                        }
                    }
                }
            }   else if (newQueryAnd.length==1&&newQueryOr.length==1){
                    for (Album a : user.getAlbums()){
                        for (PhotoModel p : a.getPhotoModelsArrayList()){
                            for (String tag : p.getTagList()){
                                String toLower = tag.toLowerCase();
                                String qToLow = query.toLowerCase();
                                if (toLower.startsWith(qToLow)){
                                    filteredPhotos.add(p);
                                    adapterFilteredPhotos.notifyOfAdd();
                                    break;
                                }
                            }
                        }
                    }
            }
    }

    @Override
    public void onPhotoClick(int position) {
        PhotoModel toGoTo = adapterFilteredPhotos.photoModels.get(position);
        for (Album a : user.getAlbums()){
            if (a.getPhotoModelsArrayList().contains(toGoTo)){
                Intent intent = new Intent(this, PhotoViewActivity.class);
                intent.putExtra("photoIndex", a.getPhotoModelsArrayList().indexOf(toGoTo));
                intent.putExtra("albumIndex", user.getAlbums().indexOf(a));
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onDeleteClick(int position) {
        PhotoModel toDelete = adapterFilteredPhotos.photoModels.get(position);
        for (Album a : user.getAlbums()){
            if (a.getPhotoModelsArrayList().contains(toDelete)){
                a.getPhotoModelsArrayList().remove(toDelete);
            }
        }
        user.saveUser(getApplicationContext());
        filteredPhotos.remove(position);
        adapterFilteredPhotos.notifyOfRemoval(position);
    }

}


