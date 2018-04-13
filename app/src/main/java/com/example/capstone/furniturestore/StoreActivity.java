package com.example.capstone.furniturestore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.furniturestore.Adapter.SearchListAdapter;
import com.example.capstone.furniturestore.Adapter.ViewPagerAdapter;
import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.Models.Department;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.ViewHolder.DepartmentViewHolder;
import com.example.capstone.furniturestore.ViewHolder.ProductViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference deptDatabase,saleDatabase,CategoryDatabase;

    //search functionality

    RecyclerView searchRecycler;
    ArrayList<Category> suggestList = new ArrayList<>();
    ArrayList<String> searchString = new ArrayList<>();

    SearchListAdapter searchListAdapter;
    MaterialSearchView materialSearchView;

    Toolbar toolbar;
    ViewPager view_Pager;
    Timer timer;
    private int currentPage = 0;
    TextView textView;
    public RecyclerView department_RecyclerView, ProductInSale_RecyclerView;
    LinearLayoutManager layoutManager;
    FloatingActionButton fb_ShoppingBasket;
    LinearLayout searchList;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //Firebase Database
        deptDatabase = FirebaseDatabase.getInstance().getReference("Department");
        saleDatabase = FirebaseDatabase.getInstance().getReference("Products");
        CategoryDatabase = FirebaseDatabase.getInstance().getReference("Category");

        // prodDatabase = FirebaseDatabase.getInstance().getReference("Products");

        //Auto Pager
        view_Pager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(this);
        view_Pager.setAdapter(viewpageradapter);
        setupAutoPager();

        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);

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

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

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



        //Load Department
        load_Department();


        load_productInSaleItems();

        CategoryDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    suggestList.add(category);
                    searchString.add(category.getCategoryName());
                    i++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        load_SearchItems();

    }


    public  void load_Department(){

        //Recycler View
        department_RecyclerView = (RecyclerView)findViewById(R.id.recycle_dept);
        department_RecyclerView.setHasFixedSize(true);
        department_RecyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        department_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

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

    public void load_productInSaleItems() {
        //Recycler View
        ProductInSale_RecyclerView = (RecyclerView)findViewById(R.id.recycle_sale);
        ProductInSale_RecyclerView.setHasFixedSize(true);
        ProductInSale_RecyclerView.setNestedScrollingEnabled(false);
        ProductInSale_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));



        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.product_sale_layout,ProductViewHolder.class,saleDatabase.orderByChild("ProductSale").equalTo("True").limitToFirst(2)) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, final Product model, int position) {
                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.product_Image);
                viewHolder.product_Name.setText(model.getProductName());


                viewHolder.product_saleLimit.setText(" " + model.getProductSaleLimit() + " % off");

                viewHolder.product_saleEndDate.setText("Ends "+model.getProductSaleEndDate());

                viewHolder.setClickListener(new ProductViewHolder.ItemClickListener() {
                    @Override
                    public void onClickItem(View view, int pos, boolean b) {
                        Intent intent = new Intent(StoreActivity.this, ProductDetailActivity.class);
                        intent.putExtra("ProductID", model.getProductID());
                        startActivity(intent);
                    }

                    @Override
                    public void onClick(View view, int adapterPosition, boolean b) {

                    }
                });
            }
        };

        ProductInSale_RecyclerView.setAdapter(adapter);

    }

    public void load_SearchItems(){
        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        materialSearchView.setVoiceSearch(true);
       // materialSearchView.setSuggestions();
        searchList = (LinearLayout) findViewById(R.id.search_listlayout);

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                    searchList.setVisibility(View.VISIBLE);


                   if(searchList.getVisibility() == View.VISIBLE){
                       searchListAdapter = new SearchListAdapter(suggestList,StoreActivity.this);

                       searchRecycler = (RecyclerView) findViewById(R.id.recycler_search_List);
                       searchRecycler.setHasFixedSize(true);
                       searchRecycler.setNestedScrollingEnabled(false);

                       layoutManager = new LinearLayoutManager(getBaseContext());
                       searchRecycler.setLayoutManager(new GridLayoutManager(StoreActivity.this, 1));

                       searchRecycler.setAdapter(searchListAdapter);
                   }
            }

            @Override
            public void onSearchViewClosed() {
                searchList.setVisibility(View.INVISIBLE);
                searchList.setVisibility(View.GONE);

            }
        });



        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    List<Category> lstfound = new ArrayList<Category>();
                    for(String item:searchString){
                        if(item.contains(newText))
                            searchString.add(item);
                    }


                }
                else {



                }
                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();

        } else {
            super.onBackPressed();
        }
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
          MenuItem item = menu.findItem(R.id.action_search);
          materialSearchView.setMenuItem(item);
        return true;
    }


    //Bottom Navigation Bar
    public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

        private int height;

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
            height = child.getHeight();
            return super.onLayoutChild(parent, child, layoutDirection);
        }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                           BottomNavigationView child, @NonNull
                                                   View directTargetChild, @NonNull View target,
                                           int axes, int type)
        {
            return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
                                   @NonNull View target, int dxConsumed, int dyConsumed,
                                   int dxUnconsumed, int dyUnconsumed,
                                   @ViewCompat.NestedScrollType int type)
        {
            if (dyConsumed > 0) {
                slideDown(child);
            } else if (dyConsumed < 0) {
                slideUp(child);
            }
        }

        private void slideUp(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(0).setDuration(200);
        }

        private void slideDown(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(height).setDuration(200);
        }
    }


}
