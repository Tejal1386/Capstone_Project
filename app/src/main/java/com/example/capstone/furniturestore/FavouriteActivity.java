package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.capstone.furniturestore.Adapter.FavouriteAdapter;
import com.example.capstone.furniturestore.Models.Favourite;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    //Database
    private DatabaseReference favouriteDatabase, productDatabase;

    //Toolbar
    Toolbar toolbar;

    //Recycler
    public RecyclerView favourite_RecyclerView;
    FavouriteAdapter fav_adapter;
    LinearLayoutManager layoutManager;
    Context context;
    final List<String> listProductID = new ArrayList<String>();
    final List<String> ProductID = new ArrayList<String>();

    ArrayList<Product> productList = new ArrayList<Product>();
    ArrayList<String>favouriteList = new ArrayList<>();

    //Shared Preferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User";
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";
    String UserID, UserName;

    Button btnEdit;
    TextView txtcountitem;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        context = getBaseContext();

        //Shared Preferences
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserName = (sharedPreferences.getString(Name, ""));
        UserID = (sharedPreferences.getString(Userid, ""));


        //Database
        favouriteDatabase = FirebaseDatabase.getInstance().getReference("Favourites").child(UserID);
        productDatabase = FirebaseDatabase.getInstance().getReference("Products");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" My Favourite");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        txtcountitem = (TextView) findViewById(R.id.txt_favouriteItem);

        //EditButton Favourite item
        btnEdit = (Button) findViewById(R.id.btn_Editfavourite);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavouriteActivity.this, EditFavouriteActivity.class);
                intent.putExtra("UserID", UserID);
                startActivity(intent);
            }
        });

        if(!UserID.isEmpty() && UserID != null)
        {
            //Display Favourite Items
            load_Favourite();
        }
        else {
            txtcountitem.setText(counter + " Item");
            btnEdit.setVisibility(View.INVISIBLE);
        }

    }

    public  void load_Favourite() {

        favouriteDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                for (DataSnapshot favouriteSnapshot : dataSnapshot.getChildren()) {
                    Favourite fav_product = favouriteSnapshot.getValue(Favourite.class);
                    String id = fav_product.getProduct_ID();

                    listProductID.add(fav_product.getProduct_ID());
                    favouriteList.add(fav_product.getFavorite_ID());

                    productDatabase.orderByChild("ProductID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                Product product = productSnapshot.getValue(Product.class);
                                productList.add(product);
                                counter++;
                            }

                            txtcountitem.setText(counter + " Item");

                            fav_adapter = new FavouriteAdapter(productList, FavouriteActivity.this);
                            favourite_RecyclerView = (RecyclerView) findViewById(R.id.recycle_Favourite);
                            favourite_RecyclerView.setHasFixedSize(true);
                            favourite_RecyclerView.setNestedScrollingEnabled(false);
                            layoutManager = new LinearLayoutManager(getBaseContext());
                            favourite_RecyclerView.setLayoutManager(new GridLayoutManager(FavouriteActivity.this, 2));
                            favourite_RecyclerView.setAdapter(fav_adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
