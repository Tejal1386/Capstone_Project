package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.capstone.furniturestore.Class.Products;
import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.Models.Department;
import com.example.capstone.furniturestore.ViewHolder.CategoryViewHolder;
import com.example.capstone.furniturestore.ViewHolder.DepartmentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class CategoryActivity extends AppCompatActivity {

    Intent intent;
    final Context context = this;
    public RecyclerView category_RecyclerView;
    LinearLayoutManager layoutManager;
    FirebaseDatabase database;
    private DatabaseReference categoryDatabase;
    TextView textView;

    MaterialSearchView searchView;

    Toolbar toolbar;
    ViewPager view_Pager;
    Timer timer;
    private int currentPage = 0;
    ListView listview;

    List<String> listOfString = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryDatabase =  FirebaseDatabase.getInstance().getReference("Category");


        Intent i = getIntent();
        String ID   = i.getExtras().getString("DeptID");

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

       //Recycler View

        category_RecyclerView = (RecyclerView) findViewById(R.id.recycle_category);
        category_RecyclerView.setHasFixedSize(true);
        category_RecyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        category_RecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        load_Category();
    }

    public  void load_Category(){

        FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.category_layout,CategoryViewHolder.class,categoryDatabase) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                viewHolder.Category_Name.setText(model.getCategoryName());
                Picasso.with(getBaseContext()).load(model.getCategoryImage()).into(viewHolder.Category_Image);
                Category clickitem = model;

                viewHolder.setClickListener(new CategoryViewHolder.ItemClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
                         intent.putExtra("CategoryID", model.getCategoryID());
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
