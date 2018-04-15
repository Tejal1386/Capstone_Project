package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.capstone.furniturestore.Adapter.SearchListAdapter;
import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.ViewHolder.BottomNavigationViewHolder;
import com.example.capstone.furniturestore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductInSaleActivity extends AppCompatActivity {

    public RecyclerView sale_RecyclerView;
    LinearLayoutManager layoutManager;

    private DatabaseReference saleDatabase ;
    Toolbar toolbar;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    //material search
    private SearchListAdapter searchListAdapter;
    private MaterialSearchView materialSearchView;
    private LinearLayout searchList;
    private ArrayList<Category> suggestList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_sale);

        //Firebase Connectivity
        saleDatabase = FirebaseDatabase.getInstance().getReference("Products");

        //Toolbar setting
        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(" Sales");

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

        //Bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewHolder());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_myFavoutite:
                        Intent intent = new Intent(ProductInSaleActivity.this, FavouriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_myAccount:
                        intent = new Intent(ProductInSaleActivity.this,UserAccountActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_sale:
                        intent = new Intent(ProductInSaleActivity.this,ProductInSaleActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        sale_RecyclerView = (RecyclerView) findViewById(R.id.recycle_Sale);
        sale_RecyclerView.setHasFixedSize(true);
        sale_RecyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        sale_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));



        load_ProductsInSale();

    }

    public  void load_ProductsInSale(){

         adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.product_sale_layout,ProductViewHolder.class,saleDatabase.orderByChild("ProductSale").equalTo("True")) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, final Product model, int position) {
                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.product_Image);
                viewHolder.product_Name.setText(model.getProductName());


                    viewHolder.product_saleLimit.setText(" " + model.getProductSaleLimit() + " % off");

                    viewHolder.product_saleEndDate.setText( "Ends " + model.getProductSaleEndDate());

                viewHolder.setClickListener(new ProductViewHolder.ItemClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        Intent intent = new Intent(ProductInSaleActivity.this, ProductDetailActivity.class);
                        intent.putExtra("ProductID", model.getProductID());
                        startActivity(intent);
                    }
                });
            }
        };

        sale_RecyclerView.setAdapter(adapter);

    }


}
