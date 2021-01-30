package com.divyanshu.spacestuff1;

import android.widget.ImageView;

public class RoverModel {
    private String imageURL;

    public RoverModel(String imageView) {
        this.imageURL = imageView;
    }

    public String getImageView() {
        return imageURL;
    }

    public void setImageView(String imageURL) {
        this.imageURL = imageURL;
    }
}
