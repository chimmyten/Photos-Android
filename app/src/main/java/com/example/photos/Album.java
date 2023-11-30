package com.example.photos;

import android.os.Bundle;

import java.sql.Array;
import java.util.ArrayList;

public class Album extends MainActivity{
    ArrayList<PhotoModel> photoModelsArrayList = new ArrayList<>();
    int[] photos = {R.drawable.ic_launcher_foreground};
    String albumName;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.album_page);

        setUpPhotos();
    }

    public void setUpPhotos(){
        photoModelsArrayList.add(new PhotoModel("potato", photos[0]));
    }

}
