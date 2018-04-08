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
import android.view.View;

import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductInSaleActivity extends AppCompatActivity {

    public RecyclerView sale_RecyclerView;
    LinearLayoutManager layoutManager;

    private DatabaseReference saleDatabase ;
    Toolbar toolbar;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_sale);

        //Firebase Connectivity
        saleDatabase = FirebaseDatabase.getInstance().getReference("Products");

        //Toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  toolbar.setTitleTextColor(1);
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
