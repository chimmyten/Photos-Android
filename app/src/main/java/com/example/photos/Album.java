package com.example.photos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable{
    ArrayList<PhotoModel> photoModelsArrayList = new ArrayList<>();
    String albumName;


    public Album(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<PhotoModel> getPhotoModelsArrayList() {
        return photoModelsArrayList;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setPhotoModelsArrayList(ArrayList<PhotoModel> photoModelsArrayList) {
        this.photoModelsArrayList = photoModelsArrayList;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

}
