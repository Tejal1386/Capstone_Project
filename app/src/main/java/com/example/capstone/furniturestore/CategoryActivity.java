package com.example.capstone.furniturestore;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryActivity extends AppCompatActivity {

    public RecyclerView category_RecyclerView;
    LinearLayoutManager layoutManager;
    private DatabaseReference categoryDatabase;
    Toolbar toolbar;
    FloatingActionButton fb_ShoppingBasket;
    String department_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //FireBase
        categoryDatabase =  FirebaseDatabase.getInstance().getReference("Category");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" Category");

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

        //Floating Button
        fb_ShoppingBasket = (FloatingActionButton) findViewById(R.id.fb_ShoppingBasket);

        fb_ShoppingBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        //Recycler View
        category_RecyclerView = (RecyclerView) findViewById(R.id.recycle_category);
        category_RecyclerView.setHasFixedSize(true);
        category_RecyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        category_RecyclerView.setLayoutManager(new GridLayoutManager(this, 1));


        // Get Intent here
        if(getIntent() != null){
            Intent i = getIntent();
            department_ID   = i.getExtras().getString("DeptID");
        }
        if(!department_ID.isEmpty() && department_ID != null){
            load_Category();
        }

    }

    public  void load_Category(){

        FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.category_layout,CategoryViewHolder.class,categoryDatabase.orderByChild("CategoryDepartmentID").equalTo(department_ID)) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                viewHolder.Category_Name.setText(model.getCategoryName());
                Picasso.with(getBaseContext()).load(model.getCategoryImage()).into(viewHolder.Category_Image);

                viewHolder.setClickListener(new CategoryViewHolder.ItemClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
                        intent.putExtra("CategoryID", model.getCategoryID());
                        intent.putExtra("CategoryName", model.getCategoryName());
                        startActivity(intent);
                    }
                });
            }
        };
        category_RecyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


}
