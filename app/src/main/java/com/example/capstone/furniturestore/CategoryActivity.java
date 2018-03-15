package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.capstone.furniturestore.Class.Products;
import com.example.capstone.furniturestore.Models.BlogHolder;
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
import java.util.TimerTask;

public class CategoryActivity extends AppCompatActivity {

    Intent intent;
    final Context context = this;
    public RecyclerView mBlogList;
    FirebaseDatabase database;
    private DatabaseReference prodDatabase, deptDatabase;
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

        prodDatabase = FirebaseDatabase.getInstance().getReference("Products");




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle("supreme Furniture");

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

        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new GridLayoutManager(this, 1));


    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);

        // Retrieve the SearchView and plug it into SearchManager
    /*   final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_view));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_basket) {
            Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {

        //Send a query to the database
        deptDatabase = FirebaseDatabase.getInstance().getReference("Department");

        // database = FirebaseDatabase.getInstance();
        //    mDatabase = database.getReference("Department");
        deptDatabase.keepSynced(true);

        super.onStart();

        FirebaseRecyclerAdapter<BlogHolder, StoreActivity.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BlogHolder, StoreActivity.BlogViewHolder>(BlogHolder.class, R.layout.blog_row, StoreActivity.BlogViewHolder.class, deptDatabase) {
            @Override
            protected void populateViewHolder(StoreActivity.BlogViewHolder viewHolder, BlogHolder model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }

        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolder(View itemView) {
            super((itemView));
            mView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }

            });
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.title);
            post_title.setText(title);

        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

}
