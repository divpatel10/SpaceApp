package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullScreenImage extends AppCompatActivity {
    String imageURL;
    PhotoView photoView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_full_screen_image);
        Bundle bundle = getIntent().getExtras();

        imageURL = bundle.getString("image");
        photoView = findViewById(R.id.imageViewFul);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Picasso.get().load(imageURL)
                .into(photoView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if(progressBar != null){
                            progressBar.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
}