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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.ViewHolder.CategoryViewHolder;
import com.example.capstone.furniturestore.ViewHolder.SearchListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchListActivity extends AppCompatActivity {

    public RecyclerView category_RecyclerView;
    LinearLayoutManager layoutManager;
    private DatabaseReference categoryDatabase;
    Toolbar toolbar;
    MaterialSearchView searchView;
    final List<String> searchList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        categoryDatabase =  FirebaseDatabase.getInstance().getReference("Category");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);

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

        category_RecyclerView = (RecyclerView) findViewById(R.id.recycle_search);
        category_RecyclerView.setHasFixedSize(true);
        category_RecyclerView.setNestedScrollingEnabled(false);
      //  layoutManager = new LinearLayoutManager(getBaseContext());
      // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        category_RecyclerView.setLayoutManager(new GridLayoutManager(this, 1));


       /* searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.closeSearch();
*/
        searchView.setVoiceSearch(true);


        load_Category();

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                load_Category();

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    List<String> lstfound = new ArrayList<String>();
                    for(String item:searchList){
                        if(item.contains(newText))
                            lstfound.add(item);
                    }

                    load_Category();

                }
                else {
                   load_Category();

                }
                return true;
            }
        });

    }

    public  void load_Category(){

        FirebaseRecyclerAdapter<Category,SearchListViewHolder> adapter = new FirebaseRecyclerAdapter<Category, SearchListViewHolder>(Category.class,R.layout.search_list,SearchListViewHolder.class,categoryDatabase) {
            @Override
            protected void populateViewHolder(SearchListViewHolder viewHolder, final Category model, int position) {
                    viewHolder.Category_Name.setText(model.getCategoryName());
                    searchList.add(model.getCategoryName());
                    viewHolder.setClickListener(new SearchListViewHolder.ItemClickListener() {
                        @Override
                        public void onClickItem(int pos) {
                            Intent intent = new Intent(SearchListActivity.this, ProductListActivity.class);
                            intent.putExtra("CategoryID", model.getCategoryID());
                            intent.putExtra("CategoryName", model.getCategoryName());

                            startActivity(intent);
                        }
                    });

                }
        };

        category_RecyclerView.setAdapter(adapter);

    }




    protected void onResume() {
        super.onResume();

        searchView.isSearchOpen();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();

        } else {
            super.onBackPressed();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item_menu, menu);
      //  MenuItem item = menu.findItem(R.id.action_search);
       // searchView.setMenuItem(item);
        return true;
    }

}
