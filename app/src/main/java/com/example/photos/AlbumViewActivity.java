package com.example.photos;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumViewActivity extends MainActivity implements album_recycler_view_interface{
    ArrayList<PhotoModel> photoModelsArrayList = new ArrayList<>(); //can be used for saved data later
    String albumName;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_album_view);

        RecyclerView recyclerView = findViewById(R.id.photoRecyclerView);

        setUpPhotos();

        album_page_recycler_view_adapter adapter = new album_page_recycler_view_adapter(this, photoModelsArrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button pickImageButton = findViewById(R.id.addNewPhotoButton);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        String fileName = getFileNameFromUri(imageUri);
                        adapter.addPhoto(new PhotoModel(fileName, imageUri));
                    }
                });


        pickImageButton.setOnClickListener(view -> openImagePicker());
    }

    public void setUpPhotos(){

//        photoModelsArrayList.add(new PhotoModel("potato1", photos[0]));

    }

    @Override
    public void onPhotoClick(int position) {
        Intent intent = new Intent(this, PhotoViewActivity.class);
        startActivity(intent);
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
                    }
                }
            }
        }
        return fileName;
    }


}
