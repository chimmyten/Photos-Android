package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        findViewById(R.id.createAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(HomePage.this, "Hello toast!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
    }
}