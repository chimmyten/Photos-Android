package com.example.photos;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Album> albums;

    public User() {
        this.albums = new ArrayList<>();
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public List<Album> getAlbums() {
        return this.albums;
    }
}
