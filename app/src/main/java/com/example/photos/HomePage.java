package com.example.photos;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ArrayAdapter;

import java.sql.Array;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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



        AutoCompleteTextView tag1TextView = findViewById(R.id.tag1Search);
        AutoCompleteTextView tag2TextView = findViewById(R.id.tag2Search);
        List<String> tagList = new ArrayList<>();
        for (Album a : user.getAlbums()) {
            for (PhotoModel p : a.getPhotoModelsArrayList()) {
                for (String tag : p.getTagList()) {
                    tagList.add(tag);
                }
            }
        }

        ArrayAdapter<String> tagSearchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagList);
        tag1TextView.setAdapter(tagSearchAdapter);
        tag1TextView.setThreshold(1);
        tag2TextView.setAdapter(tagSearchAdapter);
        tag2TextView.setThreshold(1);

        Spinner operatorMenu = findViewById(R.id.operatorMenu);
        ArrayAdapter<CharSequence> operatorAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.logical_operators, // You can create this array in your resources
                android.R.layout.simple_spinner_item
        );
        operatorMenu.setAdapter(operatorAdapter);
        operatorMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (tag2TextView.getText().length() == 0) {
                    performSearch(""+tag1TextView.getText());
                } else {
                    performSearch(tag1TextView.getText() + " " + operatorMenu.getSelectedItem() + " " + tag2TextView.getText());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterFilteredPhotos = new album_page_recycler_view_adapter(getApplicationContext(), filteredPhotos, this);

        switchToAlbumBtn.setOnClickListener(view -> {
            if (canSwitchToAlbums){
                recyclerView.setAdapter(albumListAdapter);
                canSwitchToAlbums = false;
                tag1TextView.setText("", false);
                tag1TextView.clearFocus();
                tag2TextView.setText("", false);
                tag2TextView.clearFocus();
                operatorMenu.clearFocus();
            } else {
                Toast.makeText(getApplicationContext(), "Already viewing albums!", Toast.LENGTH_SHORT).show();
            }
        });


        tag1TextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!canSwitchToAlbums){
                        canSwitchToAlbums = true;
                        filteredPhotos.clear();
                        for (Album a : user.getAlbums()){
                            for (PhotoModel p : a.getPhotoModelsArrayList()){
                                filteredPhotos.add(p);
                            }
                        }
                    }
                    recyclerView.setAdapter(adapterFilteredPhotos);
                }
            }
        });

        tag1TextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!canSwitchToAlbums){
                        canSwitchToAlbums = true;
                        filteredPhotos.clear();
                        for (Album a : user.getAlbums()){
                            for (PhotoModel p : a.getPhotoModelsArrayList()){
                                filteredPhotos.add(p);
                            }
                        }
                    }
                    recyclerView.setAdapter(adapterFilteredPhotos);
                }
            }
        });

        tag1TextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tag1Text = s.toString();
                if (tag2TextView.getText().length() == 0) {
                    performSearch(tag1Text);
                } else {
                    performSearch(s + " " + operatorMenu.getSelectedItem() + " " + tag2TextView.getText());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tag2TextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tag2Text = s.toString();
                if (tag2Text.length() == 0) {
                    performSearch("" + tag1TextView.getText());
                } else {
                    performSearch(tag1TextView.getText() + " " + operatorMenu.getSelectedItem() + " " + tag2Text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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


