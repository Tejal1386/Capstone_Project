package com.example.capstone.furniturestore.Class;

/**
 * Created by tejalpatel on 2018-03-06.
 */

public class BlogHolder {
    String DepartmentImage;
    String DepartmentName;
    String DepartmentID;

    public String getImage() {
        return DepartmentImage;
    }

    public void setImage(String image) {
        this.DepartmentImage = image;
    }

    public String getTitle() {
        return DepartmentName;
    }

    public void setTitle(String title) {
        this.DepartmentName = title;
    }

    public BlogHolder(String image, String Name,String ID) {
        this.DepartmentImage = image;
        this.DepartmentName = Name;
    }
    public BlogHolder()
    {

    }
}
