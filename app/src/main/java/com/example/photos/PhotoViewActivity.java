package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoViewActivity extends AppCompatActivity {

    Album passedInAlbum;
    int photoPosition;
    PhotoModel photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        Intent intent = getIntent();
        passedInAlbum = intent.getParcelableExtra("album", Album.class);
        photoPosition = intent.getIntExtra("photoIndex", 0);
        photo = passedInAlbum.getPhotoModelsArrayList().get(photoPosition);

        Button backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(view -> {
            // Finish the current activity to return to the previous one
            finish();
        });

        TextView caption = findViewById(R.id.photoCaption);
        caption.setText(photo.getCaption());

        ImageView imageView = findViewById((R.id.imageView));
        imageView.setImageURI(Uri.parse(photo.getPhotoURI()));

        Button cycleLeftBtn = findViewById(R.id.cycleLeftButton);
        cycleLeftBtn.setOnClickListener(view -> {
            cycleImage(photoPosition, "left");
        });

        Button cycleRightBtn = findViewById(R.id.cycleRightButton);
        cycleRightBtn.setOnClickListener(view -> {
            cycleImage(photoPosition, "right");
        });

    }


    public void cycleImage(int initialPos, String cycleType){
        if (cycleType.equals("left")){
            if (initialPos!=0){
                photoPosition--;
            } else {
                photoPosition = passedInAlbum.getPhotoModelsArrayList().size()-1;
            }
        } else {
            if (initialPos!=passedInAlbum.getPhotoModelsArrayList().size()-1){
                photoPosition++;
            } else {
                photoPosition = 0;
            }
        }

        photo = passedInAlbum.getPhotoModelsArrayList().get(photoPosition);
        TextView caption = findViewById(R.id.photoCaption);
        caption.setText(photo.getCaption());

        ImageView imageView = findViewById((R.id.imageView));
        imageView.setImageURI(Uri.parse(photo.getPhotoURI()));
    }
}