package com.example.capstone.furniturestore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchItemActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listview;
    MaterialSearchView searchView;

    String[] flavours = { "Sofa", "Cupboard", "Dining", "Table","Chair","Bed", "Dressing Table",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" supreme Furniture");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        listview = (ListView) findViewById(R.id.listview);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,flavours);
        listview.setAdapter(adapter);


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                listview = (ListView) findViewById(R.id.listview);
                ArrayAdapter adapter = new ArrayAdapter(SearchItemActivity.this, android.R.layout.simple_list_item_1,flavours);
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
                    for(String item:flavours){
                        if(item.contains(newText))
                            lstfound.add(item);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(SearchItemActivity.this, android.R.layout.simple_list_item_1,lstfound);
                    listview.setAdapter(adapter);

                }
                else {
                    ArrayAdapter adapter = new ArrayAdapter(SearchItemActivity.this, android.R.layout.simple_list_item_1,flavours);
                    listview.setAdapter(adapter);

                }
                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
}
