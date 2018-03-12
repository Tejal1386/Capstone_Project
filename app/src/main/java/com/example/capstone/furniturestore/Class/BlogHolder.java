package com.example.capstone.furniturestore.Class;

/**
 * Created by tejalpatel on 2018-03-06.
 */

public class BlogHolder {
    String image;
    String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BlogHolder(String image, String title) {
        this.image = image;
        this.title = title;
    }
    public BlogHolder()
    {

    }
}
