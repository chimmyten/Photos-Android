package com.example.photos;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;

public class Album extends MainActivity{
    ArrayList<PhotoModel> photoModelsArrayList = new ArrayList<>();
    int[] photos = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background};
    String albumName;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.album_page);

        RecyclerView recyclerView = findViewById(R.id.photoRecyclerView);

        setUpPhotos();

        album_page_recycler_view_adapter adapter = new album_page_recycler_view_adapter(this, photoModelsArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setUpPhotos(){

        photoModelsArrayList.add(new PhotoModel("potato1", photos[0]));
        photoModelsArrayList.add(new PhotoModel("potato2", photos[1]));
        photoModelsArrayList.add(new PhotoModel("potato3", photos[0]));


    }

}
