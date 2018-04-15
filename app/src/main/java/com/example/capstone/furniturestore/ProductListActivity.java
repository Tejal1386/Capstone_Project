package com.example.capstone.furniturestore;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.SearchRecentSuggestions;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.example.capstone.furniturestore.Adapter.SearchListAdapter;
import com.example.capstone.furniturestore.Database.Database;
import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.ViewHolder.BottomNavigationViewHolder;
import com.example.capstone.furniturestore.ViewHolder.CategoryViewHolder;
import com.example.capstone.furniturestore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;

public class ProductListActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";

    //Database reference
    private DatabaseReference productDatabase,categoryDatabase;

    //material searchview
    private SearchListAdapter searchListAdapter;
    private MaterialSearchView materialSearchView;
    private LinearLayout searchList;
    private ArrayList<Category> suggestList = new ArrayList<>();

    Toolbar toolbar;
    public RecyclerView product_RecyclerView;
    LinearLayoutManager layoutManager;
    private CounterFab fb_ShoppingBasket;
    private TextView txtCategoryName;
    private Button btnFilter;
    private String CategoryName = "",CategoryID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productDatabase =  FirebaseDatabase.getInstance().getReference("Products");
        categoryDatabase = FirebaseDatabase.getInstance().getReference("Category");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(" Products");

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


        btnFilter = (Button) findViewById(R.id.btn_Filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });

        fb_ShoppingBasket = (CounterFab) findViewById(R.id.fb_ShoppingBasket);

        fb_ShoppingBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, Cart.class);
                startActivity(intent);

            }
        });
        fb_ShoppingBasket.setCount(new Database(this).getCountCart());


        //Bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewHolder());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_myFavoutite:
                        Intent intent = new Intent(ProductListActivity.this, FavouriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_myAccount:
                        intent = new Intent(ProductListActivity.this,UserAccountActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_sale:
                        intent = new Intent(ProductListActivity.this,ProductInSaleActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        //Recycler View

        product_RecyclerView = (RecyclerView) findViewById(R.id.recycle_product);
        product_RecyclerView.setHasFixedSize(true);
        product_RecyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        product_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        productDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                for(DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {
                    Product products = productSnapshot.getValue(Product.class);
                    String x =  products.getProductName();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Get Intent here
        if(getIntent() != null){
            Intent i = getIntent();
            CategoryName   = i.getExtras().getString("CategoryName");
            CategoryID = i.getExtras().getString("CategoryID");
            txtCategoryName = (TextView) findViewById(R.id.txt_categoryName);
            txtCategoryName.setText(CategoryName);

        }
        if(!CategoryID.isEmpty() && CategoryID != null){
            load_Products();
        }

        categoryDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    suggestList.add(category);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       load_SearchItems();


    }

    public  void load_Products(){

        FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.product_layout,ProductViewHolder.class,productDatabase.orderByChild("ProductCategoryID").equalTo(CategoryID)) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, final Product model, int position) {
                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.product_Image);
                viewHolder.product_Name.setText(model.getProductName());
                viewHolder.product_Manufacturer.setText(model.getProductManufacturer());
                viewHolder.product_Sale_Price.setText("$"+String.valueOf( model.getProductSalePrice()));
                viewHolder.product_Price.setText("$"+String.valueOf(model.getProductPrice()));
                viewHolder.product_Price.setPaintFlags(viewHolder.product_Price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                if( model.getProductSaleLimit() > 0) {
                    viewHolder.product_saleLimit.setVisibility(View.VISIBLE);
                    viewHolder.product_saleLimit.setText(" " + model.getProductSaleLimit() + " off");
                }

                Double price = model.getProductSalePrice();
                if (price>=75.0){
                    viewHolder.product_Shipping.setText("Free Shipping");
                }
                else {
                    viewHolder.product_Shipping.setText(" ");
                }

                viewHolder.setClickListener(new ProductViewHolder.ItemClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        intent.putExtra("ProductID", model.getProductID());
                        startActivity(intent);
                    }
                });
            }
        };
       product_RecyclerView.setAdapter(adapter);

    }

    public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
        public final static String AUTHORITY = "com.example.MySuggestionProvider";
        public final static int MODE = DATABASE_MODE_QUERIES;

        public MySuggestionProvider() {
            setupSuggestions(AUTHORITY, MODE);
        }
    }

    public void load_SearchItems(){

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        materialSearchView.setEllipsize(true);
        materialSearchView.setVoiceSearch(true);

        //  String[] suggestionlist = new String[searchString.size()];
        // suggestionlist = searchString.toArray(suggestionlist);

        //   materialSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        Intent intent  = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    StoreActivity.MySuggestionProvider.AUTHORITY, StoreActivity.MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }

        searchList = (LinearLayout) findViewById(R.id.search_listlayout);

        final ListView listView = (ListView) findViewById(R.id.list_Search);

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchList.setVisibility(View.VISIBLE);
                materialSearchView.setVisibility(View.VISIBLE);
                // remove back arrow to toolbar
                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                }
            }

            @Override
            public void onSearchViewClosed() {
                searchList.setVisibility(View.GONE);
                materialSearchView.setVisibility(View.GONE);
                // add back arrow to toolbar
                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }

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
                    ArrayList<Category> lstfound = new ArrayList<Category>();
                    for(Category item:suggestList){
                        if(item.getCategoryName().contains(newText))
                            lstfound.add(item);
                    }

                    searchListAdapter = new SearchListAdapter(ProductListActivity.this, lstfound);
                    listView.setAdapter(searchListAdapter);

                }

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ProductListActivity.this, ProductListActivity.class);
                intent.putExtra("CategoryID",searchListAdapter.getItem(position).getCategoryID() );
                intent.putExtra("CategoryName", searchListAdapter.getItem(position).getCategoryName());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    materialSearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();

        } else {
            super.onBackPressed();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
