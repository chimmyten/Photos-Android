package com.example.photos;

import android.net.Uri;

public class PhotoModel {
    String caption;
    Uri photoURI;

    public PhotoModel(String caption, Uri photoURI) {
        this.caption = caption;
        this.photoURI = photoURI;
    }

    public String getCaption() {
        return caption;
    }

    public Uri getPhotoURI() {
        return photoURI;
    }
}
