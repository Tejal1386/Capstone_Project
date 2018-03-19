package com.example.capstone.furniturestore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.capstone.furniturestore.Adapter.SearchListAdapter;
import com.example.capstone.furniturestore.Models.SearchItems;
import com.example.capstone.furniturestore.R;

import java.util.ArrayList;

public class TestSearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView search_recyclerView;
    SearchListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SearchItems> arrayList = new ArrayList<>();

    String[] products = {"Test","Test1","Test2","Test3","Test4a"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search_recyclerView = (RecyclerView) findViewById(R.id.searchList_RecyclerView);
        layoutManager = new LinearLayoutManager(this);
        search_recyclerView.setLayoutManager(layoutManager);
        search_recyclerView.setHasFixedSize(true);

        int count = 0;


    }
}
