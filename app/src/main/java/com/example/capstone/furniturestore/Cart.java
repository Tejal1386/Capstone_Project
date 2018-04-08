package com.example.capstone.furniturestore;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.furniturestore.Database.Database;
import com.example.capstone.furniturestore.Helper.RecyclerItemTouchHelper;
import com.example.capstone.furniturestore.Interface.RecyclerItemTouchHelperListener;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.Models.Request;
import com.example.capstone.furniturestore.ViewHolder.CartAdapter;
import com.example.capstone.furniturestore.ViewHolder.CartViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    private static final String TAG = "data";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout rootLayout;

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
 rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        //Swipe to delete

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);





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


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartViewHolder)
        {
            String name = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
final Product  deleteItem = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());

            final int deleteIndex = viewHolder.getAdapterPosition();

             adapter.removeItem(deleteIndex);
             new Database(getBaseContext()).removeFromCart(deleteItem.getProductID());


            float total = 0;
            List<Product> orders = new Database(getBaseContext()).getCarts();
            for(Product item :orders)
                total +=(Float.parseFloat(item.getProductPricenew()))*(Integer.parseInt(item.getProductQunt()));
            txtTotalPrice.setText("$"+total+"");

            //make snackbar
            Snackbar snackbar = Snackbar.make(rootLayout,name + "removed from cart !" ,Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
    @Override
    public void onClick(View v) {
              adapter.restoreItem(deleteItem,deleteIndex);
              new Database(getBaseContext()).addToCart(deleteItem);
//update txttotal
//calculate total price
            float total = 0;
           List<Product> orders = new Database(getBaseContext()).getCarts();
           for(Product item :orders)
            total +=(Float.parseFloat(item.getProductPricenew()))*(Integer.parseInt(item.getProductQunt()));
            txtTotalPrice.setText("$"+total+"");



    }
});
snackbar.setActionTextColor(Color.YELLOW);
snackbar.show();
        }
    }
}

