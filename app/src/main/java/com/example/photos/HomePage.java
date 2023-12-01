package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        List<Album> albums = new ArrayList<>();
        albums.add(new Album("album5"));
        albums.add(new Album("album2"));
        albums.add(new Album("album3"));
        RecyclerView albumList = findViewById(R.id.albumList);
        albumList.setLayoutManager(new LinearLayoutManager(this));
        albumList.setAdapter(new AlbumListAdapter(getApplicationContext(), albums));

        findViewById(R.id.createAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(HomePage.this, "Hello toast!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
    }
}