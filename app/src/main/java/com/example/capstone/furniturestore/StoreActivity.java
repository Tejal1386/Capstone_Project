package com.example.capstone.furniturestore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.furniturestore.Adapter.ViewPagerAdapter;
import com.example.capstone.furniturestore.Models.Department;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.ViewHolder.DepartmentViewHolder;
import com.example.capstone.furniturestore.ViewHolder.ProductViewHolder;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StoreActivity extends AppCompatActivity {

    FirebaseDatabase database;
    private DatabaseReference deptDatabase,prodDatabase;

    //search functionality

    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    MaterialSearchView materialSearchView;
    String[] list;

    Toolbar toolbar;
    ViewPager view_Pager;
    Timer timer;
    private int currentPage = 0;
    TextView textView;
    public RecyclerView department_RecyclerView;
    LinearLayoutManager layoutManager;
    FloatingActionButton fb_ShoppingBasket;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //Firebase Database
        deptDatabase = FirebaseDatabase.getInstance().getReference("Department");
       // prodDatabase = FirebaseDatabase.getInstance().getReference("Products");

        //Auto Pager
        view_Pager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(this);
        view_Pager.setAdapter(viewpageradapter);
        setupAutoPager();

        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle("supreme furniture");

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

        //floating button
        fb_ShoppingBasket = (FloatingActionButton) findViewById(R.id.fb_ShoppingBasket);

        fb_ShoppingBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });


        //Tagline taxBox
        textView = (TextView) findViewById(R.id.textviewmarquee);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSelected(true);
        textView.setSingleLine();

        //Bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_myFavoutite:
                        intent = new Intent(StoreActivity.this,FavouriteActivity.class);
                        startActivity(intent);

                        // Toast.makeText(StoreActivity.this,"My Favourite",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_myAccount:
                        intent = new Intent(StoreActivity.this,UserAccountActivity.class);
                        startActivity(intent);


                        Toast.makeText(StoreActivity.this,"My Account",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_sale:

                        intent = new Intent(StoreActivity.this,ProductInSaleActivity.class);
                        startActivity(intent);

                        break;

                }
                return true;
            }
        });


        //Recycler View
        department_RecyclerView = (RecyclerView)findViewById(R.id.recycle_dept);
        department_RecyclerView.setHasFixedSize(true);
        department_RecyclerView.setNestedScrollingEnabled(false);
        department_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //Load Department
        load_Department();




        // load_search();
    }



   /* public void load_search(){

        list = new String[]{"Clipcodes", "Android Tutorials", "Youtube Clipcodes Tutorials", "SearchView Clicodes", "Android Clipcodes", "Tutorials Clipcodes"};

        //  materialSearchView = (MaterialSearchView)findViewById(R.id.searchView);
        materialSearchView.clearFocus();
        materialSearchView.setSuggestions(list);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Here Create your filtering
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //You can make change realtime if you typing here
                //See my tutorials for filtering with ListView
                return false;
            }
        });


    }*/

    public  void load_Department(){

        FirebaseRecyclerAdapter<Department,DepartmentViewHolder> adapter = new FirebaseRecyclerAdapter<Department, DepartmentViewHolder>(Department.class,R.layout.department_layout,DepartmentViewHolder.class,deptDatabase) {
            @Override
            protected void populateViewHolder(DepartmentViewHolder viewHolder, final Department model, int position) {
                viewHolder.department_Name.setText(model.getDepartmentName());
                Picasso.with(getBaseContext()).load(model.getDepartmentImage()).into(viewHolder.department_Image);
                Department clickitem = model;
                viewHolder.setClickListener(new DepartmentViewHolder.ItemClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        Intent intent = new Intent(StoreActivity.this,CategoryActivity.class);
                        intent.putExtra("DeptID",model.getDepartmentID());
                        startActivity(intent);
                    }
                });

            }
        };
        department_RecyclerView.setAdapter(adapter);

    }



    private void setupAutoPager()
    {
        view_Pager.setCurrentItem(0);

        // Timer for auto sliding
        timer  = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(currentPage<=5){
                            view_Pager.setCurrentItem(currentPage);
                            currentPage++;
                        }else{
                            currentPage = 0;
                            view_Pager.setCurrentItem(currentPage);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item_menu, menu);
        //  MenuItem item = menu.findItem(R.id.action_search);
        //  materialSearchView.setMenuItem(item);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), SearchItemActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
