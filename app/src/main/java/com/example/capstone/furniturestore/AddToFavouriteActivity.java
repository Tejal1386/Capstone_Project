package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.capstone.furniturestore.Models.Favourite;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddToFavouriteActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";


    String  ProductID,UserID;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_favourite);

        mDatabase = FirebaseDatabase.getInstance().getReference("Favourites");

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  Username = (sharedPreferences.getString(Name, ""));
        UserID = (sharedPreferences.getString(Userid, ""));


        intent = getIntent();
        ProductID = intent.getExtras().getString("ProductID");
        Toast.makeText(this,ProductID,Toast.LENGTH_LONG).show();

        add_favourite();
    }

    public void add_favourite(){
        String ID = mDatabase.push().getKey();

        if (UserID != "") {
            String title = "my Fav";
            String description = "description";
            Favourite favourite = new Favourite(UserID, ProductID, ID, title, description);
            mDatabase.child(ID).setValue(favourite);
            //    intent = new Intent(AddToFavouriteActivity.this, ProductDetailActivity.class);
            startActivity(intent);
        }

    }
}
