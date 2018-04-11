package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.furniturestore.Adapter.AddressAdapter;
import com.example.capstone.furniturestore.Adapter.FavouriteAdapter;
import com.example.capstone.furniturestore.Models.Address;
import com.example.capstone.furniturestore.Models.Favourite;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.Models.User;
import com.example.capstone.furniturestore.ViewHolder.AddressViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    Toolbar toolbar;
    public TextView tvFullName;
    public TextView tvAddress;
    public TextView txt_AddAddress;

    EditText edit_Fullname, edit_Address, edit_City, edit_State, edit_PhoneNumber, edit_postalcode;
    Button buttonSaveaddress;
    DatabaseReference addressDatabase;

    public RecyclerView Address_RecyclerView;

    LinearLayoutManager layoutManager;

    AddressAdapter add_adapter;

    final List<String> listaddressID = new ArrayList<String>();
    ArrayList<Address> addressList = new ArrayList<Address>();



    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User";
    public static final String Userid = "UseridKey";
    public static final String Name = "UserNameKey";
    public static final String Userpassword = "UserpasswordKey";

    String UserID, UserName, UserPassword;
    //String uid;

    String FullName, Address, City, State, PhoneNumber, PostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserID = (sharedPreferences.getString(Userid, ""));
        UserName = (sharedPreferences.getString(Name, ""));
        UserPassword = (sharedPreferences.getString(Userpassword, ""));


        addressDatabase = FirebaseDatabase.getInstance().getReference("User");




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle("Account Information");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed(); // Implemented by activity
            }
        });


        //calling save address layout
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        txt_AddAddress = (TextView) findViewById(R.id.txt_addaddress);

        txt_AddAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(R.layout.save_address);


                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                toolbar.setTitleTextColor(1);
                getSupportActionBar().setTitle("Account Information");

                // add back arrow to toolbar
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onBackPressed(); // Implemented by activity
                    }
                });


                edit_Fullname = (EditText) findViewById(R.id.editText_fullname);
                edit_Address = (EditText) findViewById(R.id.editText_address);
                edit_City = (EditText) findViewById(R.id.editText_city);
                edit_State = (EditText) findViewById(R.id.editText_state);
                edit_PhoneNumber = (EditText) findViewById(R.id.editText_phonenumber);
                edit_postalcode = (EditText) findViewById(R.id.editText_postalcode);


                buttonSaveaddress = (Button) findViewById(R.id.button_saveaddress);
                //saving address in database
                buttonSaveaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (UserID != null) {

                            FullName = edit_Fullname.getText().toString();
                            Address = edit_Address.getText().toString();
                            City = edit_City.getText().toString();
                            State = edit_State.getText().toString();
                            PhoneNumber = edit_PhoneNumber.getText().toString();
                            PostalCode = edit_postalcode.getText().toString();

                            addressDatabase = FirebaseDatabase.getInstance().getReference("User");

                            if (UserName != null) {

                                Map<String, Object> userAddressData = new HashMap<>();
                                userAddressData.put("ufullname", FullName);
                                userAddressData.put("uaddress", Address);
                                userAddressData.put("ucity", City);
                                userAddressData.put("ustate", State);
                                userAddressData.put("uphone", PhoneNumber);
                                userAddressData.put("upostalcode", PostalCode);


                                addressDatabase.child(UserID).child("address").push().setValue(userAddressData);

                                setContentView(R.layout.activity_add_address);



                                Toast.makeText(AddAddressActivity.this, "Address added successfully", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(AddAddressActivity.this, "Error. Please Log In", Toast.LENGTH_SHORT).show();

                            }


                        }

                    }
                });

            }
        });



        load_address();

    }


    public void load_address() {
        DatabaseReference addressDB = addressDatabase.child(UserID).child("address");



       addressDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                addressList.clear();



                for(DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {

                    int k=0;
                    Address newaddress = addressSnapshot.getValue(Address.class);

                    if(UserID != null){

                            addressList.add(newaddress);

                            k++;
                        }

                }


                add_adapter = new AddressAdapter(addressList,AddAddressActivity.this);

                Address_RecyclerView = (RecyclerView) findViewById(R.id.recycler_address);
                Address_RecyclerView.setHasFixedSize(true);
                Address_RecyclerView.setNestedScrollingEnabled(false);

                layoutManager = new LinearLayoutManager(getBaseContext());
                Address_RecyclerView.setLayoutManager(new GridLayoutManager(AddAddressActivity.this, 1));

                // favourite_RecyclerView.setLayoutManager(layoutManager);
                Address_RecyclerView.setAdapter(add_adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });











    }


}


