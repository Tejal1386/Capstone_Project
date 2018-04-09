package com.example.capstone.furniturestore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.capstone.furniturestore.Models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchItemActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listview;
    MaterialSearchView searchView;
    private DatabaseReference prodDatabase;
    List<String> listProductName = new ArrayList<String>();

    String[] list = {"One", "Two"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        prodDatabase = FirebaseDatabase.getInstance().getReference("Products");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" supreme Furniture");


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


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.closeSearch();
        searchView.setSuggestions(list);

        searchView.setVoiceSearch(true);


        prodDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                for(DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {
                    Product product = productSnapshot.getValue(Product.class);
                    listProductName.add(product.getProductName());

                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listview = (ListView) findViewById(R.id.productListView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listProductName);
        listview.setAdapter(adapter);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                listview = (ListView) findViewById(R.id.productListView);
                ArrayAdapter adapter = new ArrayAdapter(SearchItemActivity.this, android.R.layout.simple_list_item_1,listProductName);
                listview.setAdapter(adapter);

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
                    for(String item:listProductName){
                        if(item.contains(newText))
                            lstfound.add(item);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(SearchItemActivity.this, android.R.layout.simple_list_item_1,lstfound);
                    listview.setAdapter(adapter);

                }
                else {
                    ArrayAdapter adapter = new ArrayAdapter(SearchItemActivity.this, android.R.layout.simple_list_item_1,listProductName);
                    listview.setAdapter(adapter);

                }
                return true;
            }
        });

    }

    @Override
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
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
}
