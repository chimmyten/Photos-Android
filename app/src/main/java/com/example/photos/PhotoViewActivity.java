package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PhotoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        Button backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(view -> {
            // Finish the current activity to return to the previous one
            finish();
        });


    }
}