package com.xwing.sundae.android.model;

public class BaseImage {
    private String imageUrl;

    public BaseImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}