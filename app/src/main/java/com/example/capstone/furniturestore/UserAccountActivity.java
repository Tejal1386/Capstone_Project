package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UserAccountActivity extends AppCompatActivity {


    Button btnsignout;
    Intent intent;

    RelativeLayout relativeLayout_myorders,relativeLayout_mypurchases,relativeLayout_editaccount,relativeLayout_savedaddress,relativeLayout_terms;
    LinearLayout linearLayout_Call,linearLayout_Email;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";


    String UserID,UserName;
    String PhoneNumber = "0987654321";

    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
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



        //displaying name
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserName = (sharedPreferences.getString(Name, ""));
        UserID = (sharedPreferences.getString(Userid, ""));

        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(UserName);


        //myorders
        relativeLayout_myorders = (RelativeLayout)findViewById(R.id.layout_Myorders);
        relativeLayout_myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent  = new Intent(UserAccountActivity.this,ShoppingBasketActivity.class );
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


        relativeLayout_terms = (RelativeLayout)findViewById(R.id.layout_termsandpolicies);
        relativeLayout_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent =  new Intent(UserAccountActivity.this,TermsAndPoliciesActivity.class);
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

               Toast.makeText(UserAccountActivity.this,"Successfully signed out",Toast.LENGTH_LONG).show();

           }
       });

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






}
