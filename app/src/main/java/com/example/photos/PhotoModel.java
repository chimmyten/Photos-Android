package com.example.photos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoModel implements Serializable {
    String caption;
    String photoURI;

    List<String> tagsList = new ArrayList<>();

    String albumName;

    public PhotoModel(String caption, Uri photoURI, String albumName) {
        this.caption = caption;
        this.photoURI = photoURI.toString();
        this.albumName = albumName;
    }

    public String getCaption() {
        return caption;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public List<String> getTagList(){
        return tagsList;
    }
    public void addTag(String newTag){
        tagsList.add(newTag);
    }


}
