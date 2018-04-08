package com.example.capstone.furniturestore;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.capstone.furniturestore.Database.Database;
import com.example.capstone.furniturestore.Models.Favourite;
import com.example.capstone.furniturestore.Models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";
    private DatabaseReference productDatabase;
    Toolbar toolbar;
    CounterFab fb_ShoppingBasket;
    TextView txtProductName,txtProductMenufacturer, txtProductSalePrice, txtProductPrice, txtProductShipping ,txtProductInformation, txtShipping;
    ImageView imgProduct;

    Button btnAddToCart, btnAddToFavourite, btnView;
    ElegantNumberButton numberButton;
    Product current_product;

    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";


    String  ProductID = "", UserID="";
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productDatabase =  FirebaseDatabase.getInstance().getReference("Products");

        mDatabase = FirebaseDatabase.getInstance().getReference("Favourites");

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        UserID = (sharedPreferences.getString(Userid, ""));

        imgProduct = (ImageView) findViewById(R.id.imageproduct);
        txtProductName = (TextView) findViewById(R.id.txtproductName);
        txtProductMenufacturer = (TextView) findViewById(R.id.txtproductManufacturer);
        txtProductSalePrice = (TextView) findViewById(R.id.txtproductSalePrice);
        txtProductPrice = (TextView) findViewById(R.id.txtproductPrice);
        txtProductShipping = (TextView) findViewById(R.id.txtproductshipping);
        Intent i = getIntent();
        ProductID = i.getExtras().getString("ProductID");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //    toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" Products");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);


        btnAddToCart = (Button) findViewById(R.id.btn_AddToCart);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_product.setProductQunt(numberButton.getNumber());

                Log.e("==value", String.valueOf(current_product.getProductPrice()));
                new Database(ProductDetailActivity.this).addToCart((current_product));
                current_product.setProductImage(current_product.getProductImage());
                current_product.setProductPrice(current_product.getProductPrice());

                Toast.makeText(ProductDetailActivity.this,"Added",Toast.LENGTH_SHORT).show();
            }
        });


        btnAddToFavourite = (Button) findViewById(R.id.btn_favoiurite);

        btnAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(UserID.equals(null) || UserID ==  ""){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ProductDetailActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("LogIn First");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("LogIn First to see your Account!")
                            .setCancelable(false)

                            .setPositiveButton("LogIn", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                   Intent i = new Intent(ProductDetailActivity.this,LoginActivity.class);
                                    startActivity(i);
                                }
                            });



                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
                else {


                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ProductDetailActivity.this);
                    View promptsView = li.inflate(R.layout.add_to_favourite, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ProductDetailActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editText_title);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Add",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            String ID = mDatabase.push().getKey();

                                            if (UserID != "") {
                                                String title = userInput.getText().toString();
                                                Favourite favourite = new Favourite(ProductID, ID, title);
                                                mDatabase.child(UserID).child(ID).setValue(favourite);

                                            }


                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }


            }
        });



        //ARCore Integration button
        btnView = (Button)findViewById(R.id.btn_View);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent = new Intent(ProductDetailActivity.this, AddToFavouriteActivity.class);
              //  intent.putExtra("ProductID", ProductID);
                //startActivity(intent);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed(); // Implemented by activity
            }
        });

     fb_ShoppingBasket = (CounterFab) findViewById(R.id.fb_ShoppingBasket);

        fb_ShoppingBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, Cart.class);
                startActivity(intent);

            }
        });


        txtProductInformation = (TextView) findViewById(R.id.txt_Information);
        txtProductInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this,ProductInformationActivity.class);
                intent.putExtra("ProductID", ProductID);
                startActivity(intent);
            }
        });

        fb_ShoppingBasket.setCount(new Database(this).getCountCart());

        productDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                for(DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {
                    Product products = productSnapshot.getValue(Product.class);
                    String prod_id = products.getProductID();

                    if(prod_id.equals(ProductID)){
                        current_product=products;
                        Picasso.with(getBaseContext()).load(products.getProductImage()).into(imgProduct);
                        txtProductName.setText(products.getProductName());
                        txtProductMenufacturer.setText(products.getProductManufacturer());
                        txtProductPrice.setText(String.valueOf(products.getProductPrice()));
                        txtProductSalePrice.setText(String.valueOf(products.getProductSalePrice()));
                        if(products.getProductSalePrice() > 75.0) {
                            txtProductShipping.setText("Free Shipping");
                        }
                        else {
                            txtProductShipping.setText("");
                        }

                    }

                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    protected void onResume() {

        super.onResume();
        fb_ShoppingBasket.setCount(new Database(this).getCountCart());
    }

}
