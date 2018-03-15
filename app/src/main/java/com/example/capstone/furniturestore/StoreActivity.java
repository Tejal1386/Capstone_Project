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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.capstone.furniturestore.Adapter.SearchListAdapter;
import com.example.capstone.furniturestore.Models.BlogHolder;
import com.example.capstone.furniturestore.Adapter.ViewPagerAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class StoreActivity extends AppCompatActivity {

    Intent intent;
    final Context context = this;
    public RecyclerView mBlogList;
    FirebaseDatabase database;
    private DatabaseReference deptDatabase;
    TextView textView;

    MaterialSearchView searchView;

    private static Context mContext;

    Toolbar toolbar;
    ViewPager view_Pager;
    Timer timer;
    private int currentPage = 0;
    ListView listview;



    RecyclerView SearchList_RecyclerView;
    SearchListAdapter searchListAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //Send a query to the database
        deptDatabase = FirebaseDatabase.getInstance().getReference("Department");


        mContext = context.getApplicationContext();

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

        mBlogList = (RecyclerView)findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new GridLayoutManager(this, 2));


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
        getMenuInflater().inflate(R.menu.menu_item, menu);

     //   MenuItem item = menu.findItem(R.id.action_search);
      //  searchView.setMenuItem(item);

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

        //noinspection SimplifiableIfStatement
       if (id == R.id.action_basket) {
            Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart()
    {


        deptDatabase.keepSynced(true);

        super.onStart();
        setupAutoPager();
        FirebaseRecyclerAdapter<BlogHolder, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BlogHolder, BlogViewHolder>(BlogHolder.class, R.layout.blog_row,BlogViewHolder.class,deptDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, BlogHolder model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }

        };




        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }
    public  static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public BlogViewHolder(View itemView)
        {
            super((itemView));
            mView=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, CategoryActivity.class);
                    mContext.startActivity(intent);
                }

            });
        }
        public  void setTitle(String title)
        {
            TextView post_title =(TextView)mView.findViewById(R.id.title);
            post_title.setText(title);

        }
        public void setImage(Context ctx, String image)
        {
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }


}
