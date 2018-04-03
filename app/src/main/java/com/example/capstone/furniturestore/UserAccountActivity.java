package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserAccountActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";

    String UserID,UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserName = (sharedPreferences.getString(Name, ""));
        UserID = (sharedPreferences.getString(Userid, ""));

        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(UserName);

    }
}
