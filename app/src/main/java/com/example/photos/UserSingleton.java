package com.example.photos;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserSingleton implements Serializable{
    private static UserSingleton instance;
    private static List<Album> albums;

    private UserSingleton() {
        this.albums = new ArrayList<>();
    }

    public static void saveUser(Context context, UserSingleton user) {

    }
    public static synchronized UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void deleteAlbum(int position) {
        albums.remove(position);
    }

    public void renameAlbum(int position, String name) {
       albums.get(position).setAlbumName(name);
    }
}
