package com.example.photos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoViewActivity extends AppCompatActivity implements tag_recycler_view_interface{

    Album passedInAlbum;
    int photoPosition;
    PhotoModel photo;

    String tagToAdd;

    RecyclerView recyclerView;
    tag_recycler_view_adapter adapter;
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
        cycleLeftBtn.setOnClickListener(view -> cycleImage(photoPosition, "left"));

        Button cycleRightBtn = findViewById(R.id.cycleRightButton);
        cycleRightBtn.setOnClickListener(view -> cycleImage(photoPosition, "right"));


        recyclerView = findViewById(R.id.tagRecyclerView);
        adapter = new tag_recycler_view_adapter(this, photo.getTagList(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        adapter = new tag_recycler_view_adapter(this, photo.getTagList(), this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDeleteTagClick(int position) {
        photo.getTagList().remove(position);
        Toast.makeText(getApplicationContext(), "Tag Deleted!", Toast.LENGTH_SHORT).show();
        adapter.notifyOfRemoval(position);
    }


    public void openPopup(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tag_input_popup, null);
        alertDialogBuilder.setView(dialogView);

        final EditText userInput = dialogView.findViewById(R.id.userInput);

        alertDialogBuilder
                .setTitle("Enter Tag To Add")
                .setPositiveButton("OK", (dialog, id) -> {
                    tagToAdd = userInput.getText().toString();
                    String[] split = tagToAdd.split("=",2);
                    if (split.length!=2||(!split[0].equals("Location")&&!split[0].equals("People"))||split[1].equals("")||passedInAlbum.getPhotoModelsArrayList().get(photoPosition).getTagList().contains(tagToAdd)){
                        Toast.makeText(getApplicationContext(), "Wrong tag input format or duplicate tag, please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        photo.addTag(tagToAdd);
                        adapter.notifyOfAdd();
                    }

                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}