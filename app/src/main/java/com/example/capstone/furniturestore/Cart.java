package com.example.capstone.furniturestore;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.furniturestore.Database.Database;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.Models.Request;
import com.example.capstone.furniturestore.ViewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    private static final String TAG = "data";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

  public   TextView txtTotalPrice;
    TextView txtname;
  Button btnplace;
  TextView city;
  Number nbr;
  TextView province;

    List<Product> cart = new ArrayList<>();
    CartAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");


        recyclerView = (RecyclerView) findViewById(R.id.listCart);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnplace = (Button) findViewById(R.id.btn_placeorder);
btnplace.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        List<Product> orders = new Database(getApplicationContext()).getCarts();
        if(orders.size()==0){
            Toast.makeText(getApplicationContext(),"First add item in cart",Toast.LENGTH_SHORT).show();
        }else {
            showAlertDialog();
        }
    }
});


        loadListOrder();}
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Enter your address: ");

        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(lp);

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final EditText edtname = new EditText(Cart.this);
        final EditText edtphone= new EditText(Cart.this);
        final EditText edtAddress =new EditText(Cart.this);

        edtphone.setLayoutParams(lp);
        edtname.setLayoutParams(lp);
        edtAddress.setLayoutParams(lp);

        edtphone.setHint("Enter Phone");
        edtname.setHint("Enter Name");
        edtAddress.setHint("Enter Address");

        linearLayout.addView(edtname);
        linearLayout.addView(edtphone);
        linearLayout.addView(edtAddress);

        alertDialog.setView(linearLayout);// add edit text to alert dialog


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Request request = new Request(
                        edtname.getText().toString(),
                        edtphone.getText().toString(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart  );
                //submit to firebase
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank you , order placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        alertDialog.show();
    }
    private void loadListOrder() {
        cart.clear();
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);


        float total = 0;

        for(Product order:cart) {
             total+=(Float.parseFloat(String.valueOf(order.getProductPricenew())));//*(Integer.parseInt(order.getProtductQunt()));
            //  Locale locale = new Locale("en","US");
            //  NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

          // Log.e("===value", order.getProductQunt());
            Log.e("==value", String.valueOf(order.getProductPricenew()));
            Log.e("===2nd value", order.getProductName());
            txtTotalPrice.setText(total+"");//order.getProductName());
            //.format(total));

        }
    }


}

