package com.example.capstone.furniturestore;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Timer;

public class ProductListActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";
    public RecyclerView product_RecyclerView;
    LinearLayoutManager layoutManager;
    private DatabaseReference productDatabase;
    Toolbar toolbar;
    FloatingActionButton fb_ShoppingBasket;
    String CategoryName = "",CategoryID;
    TextView txtCategoryName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productDatabase =  FirebaseDatabase.getInstance().getReference("Products");

        Intent i = getIntent();
        CategoryName   = i.getExtras().getString("CategoryName");
        CategoryID = i.getExtras().getString("CategoryID");
        txtCategoryName = (TextView) findViewById(R.id.txt_categoryName);
        txtCategoryName.setText(CategoryName);
        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" Products");

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

        fb_ShoppingBasket = (FloatingActionButton) findViewById(R.id.fb_ShoppingBasket);

        fb_ShoppingBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, ShoppingBasketActivity.class);
                startActivity(intent);

            }
        });

        //Recycler View

        product_RecyclerView = (RecyclerView) findViewById(R.id.recycle_product);
        product_RecyclerView.setHasFixedSize(true);
        product_RecyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        product_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        load_Products();

    }

    public  void load_Products(){

        FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.product_layout,ProductViewHolder.class,productDatabase.orderByChild("ProductCategoryID").equalTo(CategoryID)) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, final Product model, int position) {
                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.product_Image);
                viewHolder.product_Name.setText(model.getProductName());
                viewHolder.product_Manufacturer.setText(model.getProductManufacturer());
                viewHolder.product_Sale_Price.setText("$"+String.valueOf( model.getProductSalePrice()));
                viewHolder.product_Price.setText("$"+String.valueOf(model.getProductPrice()));
                viewHolder.product_Price.setPaintFlags(viewHolder.product_Price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                if( String.valueOf(model.getProductSaleLimit()) != "0") {
                    viewHolder.product_saleLimit.setVisibility(View.VISIBLE);
                    viewHolder.product_saleLimit.setText(" " + model.getProductSaleLimit() + " off");
                }

                Double price = model.getProductSalePrice();
                if (price>=75.0){
                    viewHolder.product_Shipping.setText("Free Shipping");
                }
                else {
                    viewHolder.product_Shipping.setText(" ");
                }

                viewHolder.setClickListener(new ProductViewHolder.ItemClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        intent.putExtra("ProductID", model.getProductID());
                        startActivity(intent);
                    }
                });
            }
        };
       product_RecyclerView.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item_menu, menu);
        return true;
    }

}
