package com.example.capstone.furniturestore;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Timer;

public class ProductListActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager view_Pager;
    Timer timer;
    private int currentPage = 0;

    String[] flavours = { "Sofa", "Cupboard", "Dining", "Table","Chair","Bed", "Dressing Table",""};


    ListView listproduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

    }

}
