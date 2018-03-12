package com.example.capstone.furniturestore.Class;

/**
 * Created by tejalpatel on 2018-03-06.
 */

public class User {
    private String userId;
    private String userName;
    private String password;

    public User(){

    }

    public User(String userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
}
