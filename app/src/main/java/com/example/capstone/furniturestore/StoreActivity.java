package com.example.capstone.furniturestore;

import android.content.Intent;
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

import com.example.capstone.furniturestore.Adapter.ViewPagerAdapter;
import com.example.capstone.furniturestore.Models.Department;
import com.example.capstone.furniturestore.ViewHolder.DepartmentViewHolder;
import com.squareup.picasso.Picasso;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class StoreActivity extends AppCompatActivity {

    FirebaseDatabase database;
    private DatabaseReference deptDatabase;

    Toolbar toolbar;
    ViewPager view_Pager;
    Timer timer;
    private int currentPage = 0;
    TextView textView;
    public RecyclerView department_RecyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        deptDatabase = FirebaseDatabase.getInstance().getReference("Department");


        view_Pager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(this);
        view_Pager.setAdapter(viewpageradapter);
        setupAutoPager();

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

        textView = (TextView) findViewById(R.id.textviewmarquee);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSelected(true);
        textView.setSingleLine();

         //Recycler View

        department_RecyclerView = (RecyclerView)findViewById(R.id.recycle_dept);
        department_RecyclerView.setHasFixedSize(true);
        department_RecyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        department_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        load_Department();

    }

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
