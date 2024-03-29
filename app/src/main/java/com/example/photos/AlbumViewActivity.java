package com.example.photos;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumViewActivity extends AppCompatActivity implements album_recycler_view_interface{

    int albumIndex;
    Album currentAlbum;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    album_page_recycler_view_adapter adapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_album_view);
        Intent intent = getIntent();
        user = User.loadUser(getApplicationContext());
        albumIndex = intent.getIntExtra("clickedAlbumPos", 0);

        currentAlbum = user.getAlbums().get(albumIndex);
        TextView albumName = findViewById(R.id.albumName);
        albumName.setText(currentAlbum.getAlbumName());
        RecyclerView recyclerView = findViewById(R.id.photoRecyclerView);


        adapter = new album_page_recycler_view_adapter(this, currentAlbum.getPhotoModelsArrayList(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button backButton = findViewById(R.id.backToHomeButton);

        backButton.setOnClickListener(view -> {
            Intent intent2 = new Intent(AlbumViewActivity.this, HomePage.class);
            startActivity(intent2);
            finish();
        });


        Button pickImageButton = findViewById(R.id.addNewPhotoButton);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        String fileName = getFileNameFromUri(imageUri);
                        if (!checkForPhoto(fileName)) {
                            ContentResolver contentResolver = getContentResolver();
                            // Take persistable URI permission for the image
                            contentResolver.takePersistableUriPermission(imageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                            currentAlbum.getPhotoModelsArrayList().add(new PhotoModel(fileName, imageUri, currentAlbum.getAlbumName()));
                            user.saveUser(getApplicationContext());
                            adapter.notifyOfAdd();
                        } else {
                            Toast.makeText(getApplicationContext(), "That image already exists in this album", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        pickImageButton.setOnClickListener(view -> openImagePicker());
    }

    @Override
    public void onPhotoClick(int position) {
        Intent intent = new Intent(this, PhotoViewActivity.class);
        intent.putExtra("photoIndex", position);
        intent.putExtra("albumIndex", albumIndex);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteClick(int position) {
        currentAlbum.getPhotoModelsArrayList().remove(position);
        user.saveUser(getApplicationContext());
        adapter.notifyOfRemoval(position);
    }

    // Method to launch image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }


    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri != null) {
            ContentResolver contentResolver = getContentResolver();
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        fileName = cursor.getString(displayNameIndex);
                        // Remove file extension if present
                        int extensionIndex = fileName.lastIndexOf(".");
                        if (extensionIndex != -1) {
                            fileName = fileName.substring(0, extensionIndex);
                        }
                    }
                }
            }
        }
        return fileName;
    }

    public boolean checkForPhoto(String fileName){
        for (PhotoModel p : currentAlbum.photoModelsArrayList){
            if (p.caption.equals(fileName)){
                return true;
            }
        }
        return false;
    }
}
