package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullScreenImage extends AppCompatActivity {
    String imageURL;
    PhotoView photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        Bundle bundle = getIntent().getExtras();
        imageURL = bundle.getString("image");
        photoView = findViewById(R.id.imageViewFul);
        Picasso.get().load(imageURL).into(photoView);
        getSupportActionBar().hide();
    }
}