package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.furniturestore.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText editText_username, editText_password;
    private Button btn_login;
    private TextView txtlink_SignUp;
    Intent intent;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Name = "UserNameKey";
    public static final String Userid = "UseridKey";



    Toolbar toolbar;

    private Integer checkUser,checkPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" LogIn");

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



        editText_username = (EditText)  findViewById(R.id.editTxt_UserName);
        editText_password = (EditText) findViewById(R.id.editTxt_Password);
        btn_login = (Button) findViewById(R.id.btnLogin);
        txtlink_SignUp = (TextView) findViewById(R.id.txtlinkSignUp);

        txtlink_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser();
            }
        });
    }
    private void getUser()
    {

        final String userName = editText_username.getText().toString();
        final String passWord = editText_password.getText().toString();



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                checkUser = 0;
                checkPassword = 0;
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    User user = userSnapshot.getValue(User.class);

                    String username = user.getUserName();
                    String password = user.getPassword();

                    if (username.equals(userName)) {
                        checkUser++;
                        if(password.equals(passWord)) {
                            editor.putString(Userid,user.getUserId());
                            editor.putString(Name, username);
                            editor.apply();



                            checkPassword++;
                        }

                    }

                }

                if(checkUser>0) {
                    if(checkPassword>0) {
                        intent = new Intent(LoginActivity.this, StoreActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_LONG).show();;

                    }
                }
                else {
                    Toast.makeText(LoginActivity.this,"UserName dose not exists",Toast.LENGTH_LONG).show();;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
