package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.Models.Favourite;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.Models.User;
import com.example.capstone.furniturestore.ViewHolder.CategoryViewHolder;
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

    public RecyclerView favourite_RecyclerView;
    LinearLayoutManager layoutManager;
    private DatabaseReference favouriteDatabase, productDatabase;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";
    String UserID,UserName;
   final List<String> listProductID = new ArrayList<String>();
   String[] list = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserName = (sharedPreferences.getString(Name, ""));
        UserID = (sharedPreferences.getString(Userid, ""));

        favouriteDatabase =  FirebaseDatabase.getInstance().getReference("Favourites");
        //  favouriteDatabase.orderByChild("user_ID").equalTo(UserID);
        productDatabase = FirebaseDatabase.getInstance().getReference("Products");


        setContentView(R.layout.activity_favourite);



        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" My Favourite");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed(); // Implemented by activity
            }
        });





        favourite_RecyclerView = (RecyclerView) findViewById(R.id.recycle_Favourite);
        favourite_RecyclerView.setHasFixedSize(true);
        favourite_RecyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        favourite_RecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        load_Favourite();


    }

    public  void load_Favourite(){

        favouriteDatabase.orderByChild("user_ID").equalTo(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                for(DataSnapshot favouriteSnapshot : dataSnapshot.getChildren())
                {
                    Favourite fav_product = favouriteSnapshot.getValue(Favourite.class);
                    listProductID.add(fav_product.getProduct_ID());


                    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class, R.layout.product_layout, ProductViewHolder.class, productDatabase.orderByChild("ProductID").equalTo(listProductID.get(i))) {
                        @Override
                        protected void populateViewHolder(ProductViewHolder viewHolder, final Product model, int position) {
                            Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.product_Image);
                            viewHolder.product_Name.setText(model.getProductName());
                            viewHolder.product_Manufacturer.setText(model.getProductManufacturer());
                            viewHolder.product_Sale_Price.setText("$" + String.valueOf(model.getProductSalePrice()));
                            viewHolder.product_Price.setText("$" + String.valueOf(model.getProductPrice()));
                            viewHolder.product_Price.setPaintFlags(viewHolder.product_Price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            Double price = model.getProductSalePrice();
                            if (price >= 75.0) {
                                viewHolder.product_Shipping.setText("Free Shipping");
                            } else {
                                viewHolder.product_Shipping.setText(" ");
                            }


                            viewHolder.setClickListener(new ProductViewHolder.ItemClickListener() {
                                @Override
                                public void onClickItem(int pos) {
                                    Intent intent = new Intent(FavouriteActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("ProductID", model.getProductID());
                                    startActivity(intent);
                                }
                            });
                        }
                    };


                    favourite_RecyclerView.setAdapter(adapter);




                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

}
