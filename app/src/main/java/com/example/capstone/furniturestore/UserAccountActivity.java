package com.example.capstone.furniturestore;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.furniturestore.Adapter.SearchListAdapter;
import com.example.capstone.furniturestore.Models.Category;
import com.example.capstone.furniturestore.ViewHolder.BottomNavigationViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class UserAccountActivity extends AppCompatActivity {

    //private FirebaseAuth firebaseAuth;
    Button btnsignout;
    TextView txtmyorders,txtaccountinformation,txtEmail,txtCall,txtSavedaddress;
    Intent intent;

    RelativeLayout relativeLayout_myorders,relativeLayout_mypurchases,relativeLayout_editaccount,relativeLayout_savedaddress;
    LinearLayout linearLayout_Call,linearLayout_Email;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";


    String UserID,UserName;
    String PhoneNumber = "0987654321";

    private DatabaseReference categoryDatabase;


    Toolbar toolbar;


    //material searchview
    private SearchListAdapter searchListAdapter;
    private MaterialSearchView materialSearchView;
    private LinearLayout searchList;
    private ArrayList<Category> suggestList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        categoryDatabase = FirebaseDatabase.getInstance().getReference("Category");



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("My Account");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        //Bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHolder.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_myFavoutite:
                        Intent intent = new Intent(UserAccountActivity.this, FavouriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_myAccount:
                        intent = new Intent(UserAccountActivity.this,UserAccountActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_sale:
                        intent = new Intent(UserAccountActivity.this,ProductInSaleActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        //displaying name
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserName = (sharedPreferences.getString(Name, ""));
        UserID = (sharedPreferences.getString(Userid, ""));

        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(UserName);
        TextView txtUsertagline = (TextView) findViewById(R.id.txtUsertagline);
        txtUsertagline.setText("You are logged in as: " +UserName);



        //myorders
        relativeLayout_myorders = (RelativeLayout)findViewById(R.id.layout_Myorders);
        relativeLayout_myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent  = new Intent(UserAccountActivity.this,OrderStatus.class );
                startActivity(intent);
            }
        });



        relativeLayout_mypurchases = (RelativeLayout)findViewById(R.id.layout_Mypurchases);
        relativeLayout_mypurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        relativeLayout_editaccount = (RelativeLayout)findViewById(R.id.layout_Accountinformation);
        relativeLayout_editaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =  new Intent(UserAccountActivity.this,AccountInformation.class);
                startActivity(intent);

            }
        });



        relativeLayout_savedaddress = (RelativeLayout)findViewById(R.id.layout_Savedaddress);
        relativeLayout_savedaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent =  new Intent(UserAccountActivity.this,AddAddressActivity.class);
                startActivity(intent);
            }
        });



        linearLayout_Email =  (LinearLayout)findViewById(R.id.layout_Email);
        linearLayout_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //method defined
                sendEmail();

            }
        });


        //Call
        linearLayout_Call = (LinearLayout)findViewById(R.id.layout_Call);
        linearLayout_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + PhoneNumber));
                startActivity(intent);

            }
        });



        //Signout button
        btnsignout = (Button)findViewById(R.id.button_signout);
       btnsignout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               sharedPreferences  = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.clear();
               editor.commit();
               finish();

               intent = new Intent(UserAccountActivity.this,MainActivity.class);
               startActivity(intent);

               //firebaseAuth.getInstance().signOut();
               Toast.makeText(UserAccountActivity.this,"signout",Toast.LENGTH_LONG).show();

           }
       });


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



    //email method
    public void sendEmail()
    {
        intent=new Intent(Intent.ACTION_SEND);
        String[] recipients={"mailto@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
        intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
        intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));
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

                    searchListAdapter = new SearchListAdapter(UserAccountActivity.this, lstfound);
                    listView.setAdapter(searchListAdapter);

                }

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(UserAccountActivity.this, ProductListActivity.class);
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
