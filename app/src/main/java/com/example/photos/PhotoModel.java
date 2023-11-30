package com.example.photos;

public class PhotoModel {
    String caption;
    int image;

    public PhotoModel(String caption, int image) {
        this.caption = caption;
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public int getImage() {
        return image;
    }
}
