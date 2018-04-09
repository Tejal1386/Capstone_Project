package com.example.capstone.furniturestore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.capstone.furniturestore.Models.Category;
import com.firebase.ui.database.FirebaseListAdapter;
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
    List<String> listProductID = new ArrayList<String>();

    String[] list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        //FireBase Database
        prodDatabase = FirebaseDatabase.getInstance().getReference("Category");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" supreme Furniture");

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
        searchView.setFocusable(true);
        searchView.setVoiceSearch(true);




        prodDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;

                for(DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {
                    Category product = productSnapshot.getValue(Category.class);
                    listProductName.add(product.getCategoryName());
                    listProductID.add(product.getCategoryID());
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        list = new String[listProductName.size()];
        int i =0;
        for (String listitem: listProductName){
            list[i] = listProductName.get(i);
            i++;
        }

        searchView.setSuggestions(list);
        listview = (ListView) findViewById(R.id.productListView);

        /*listview = (ListView) findViewById(R.id.productListView);
        FirebaseListAdapter <String> adapter = new FirebaseListAdapter<String>(this,String.class,android.R.layout.simple_list_item_1,prodDatabase) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };*/
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listProductName);

        listview.setAdapter(adapter);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                ArrayAdapter adapter = new ArrayAdapter(SearchItemActivity.this, android.R.layout.simple_list_item_1,listProductName);
                //adapter.add(listProductID);
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


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchItemActivity.this, ProductListActivity.class);
                //intent.putExtra("CategoryID" , listview.getItemAtPosition());
                // intent.putExtra("CategoryName" , listview.getItemAtPosition(position).toString());
                // startActivity(intent);
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
       // MenuItem item = menu.findItem(R.id.action_search);
        //searchView.setMenuItem(item);
        return true;
    }
}
