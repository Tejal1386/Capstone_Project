package com.example.capstone.furniturestore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone.furniturestore.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText editText_username, editText_password;
    private Button btn_register;
    private Intent intent;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase = FirebaseDatabase.getInstance().getReference("User");

        //toolBar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle(" SignUp");

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
        btn_register = (Button) findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addUser();
            }
        });

    }
    private void addUser()
    {
        String ID = mDatabase.push().getKey();
        String userName = editText_username.getText().toString();
        String password = editText_password.getText().toString();
        //String address = "";

        if(!TextUtils.isEmpty(userName)) {
            User user = new User(ID, userName, password, null);

            mDatabase.child(ID).setValue(user);
            intent = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);


        }else {
            Toast.makeText(this,"You Should enter UserName",Toast.LENGTH_LONG).show();;
        }
    }
}
