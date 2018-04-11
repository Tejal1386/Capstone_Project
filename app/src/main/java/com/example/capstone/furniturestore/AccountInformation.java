package com.example.capstone.furniturestore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;


public class AccountInformation extends AppCompatActivity {

    TextView txtedit_password,txtedit_username;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference userReference;
    FirebaseAuth auth;
    EditText editText;
   // Button btnchangepassword;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "User" ;
    public static final String Userid = "UseridKey";
    public static final String Name = "UserNameKey";
    public static final String Userpassword = "UserpasswordKey";


    String UserID,UserName,UserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);


        //editText =(EditText)findViewById(R.id.editTxt_Password);

       // auth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserID = (sharedPreferences.getString(Userid, ""));
        UserName = (sharedPreferences.getString(Name, ""));
        UserPassword =(sharedPreferences.getString(Userpassword,""));



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(1);
        getSupportActionBar().setTitle("Account Information");

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




        txtedit_password = (TextView)findViewById(R.id.txt_changepassword);
        txtedit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChangePasswordDialog();

            }
        });


        txtedit_username = (TextView)findViewById(R.id.txt_changeusername);
        txtedit_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChangeUsernameDialog();

            }
        });






    }




    /*
        private void showChangePasswordDialogold()
    {


        AlertDialog.Builder alertdialog = new AlertDialog.Builder(AccountInformation.this);
        alertdialog.setTitle("Change password");
        alertdialog.setMessage("Please fill all information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_password,null);


       final MaterialEditText edit_password = (MaterialEditText)layout_pwd.findViewById(R.id.editpassword);
       final MaterialEditText edit_Newpassword = (MaterialEditText)layout_pwd.findViewById(R.id.editNewpassword);
        final MaterialEditText edit_Repeatpassword = (MaterialEditText)layout_pwd.findViewById(R.id.editRepeatNewpassword);

        alertdialog.setView(layout_pwd);

        //button
       alertdialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //change password here

                final android.app.AlertDialog waitingdialog  = new SpotsDialog(AccountInformation.this);
                waitingdialog.show();

                //
                if(edit_password.getText().toString().equals(UserPassword))
                {
                    if(edit_Newpassword.getText().toString().equals(edit_Repeatpassword.getText().toString()))
                    {
                        Map<String,Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("password",edit_Newpassword.getText().toString());


                        DatabaseReference user_pass = FirebaseDatabase.getInstance().getReference("User");


                        user_pass.child(UserID).updateChildren(passwordUpdate).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        waitingdialog.dismiss();
                                        Toast.makeText(AccountInformation.this,"Password updated",Toast.LENGTH_LONG ).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AccountInformation.this,e.getMessage(),Toast.LENGTH_LONG ).show();
                                    }
                                });

                    }

                    else{
                        waitingdialog.dismiss();
                        Toast.makeText(AccountInformation.this,"New Password does not match",Toast.LENGTH_LONG ).show();
                        }
                }

                else{

                    Toast.makeText(AccountInformation.this,"Wrong Password ",Toast.LENGTH_LONG ).show();
                    }
            }
        });


        alertdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        });

   } */




    //showdialog method
    public void showChangePasswordDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.change_password,null);
        dialogBuilder .setView(dialogview);


        final EditText edit_OldPassword = (EditText)dialogview.findViewById(R.id.edit_oldpassword);
        final EditText edit_NewPassword = (EditText)dialogview.findViewById(R.id.edit_newpassword);


        dialogBuilder.setTitle("Change Password");
        dialogBuilder.setMessage("Enter information below");

        dialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                userReference = FirebaseDatabase.getInstance().getReference("User");

                if(UserName != null){
                    Map<String,Object> passwordUpdate = new HashMap<>();
                    passwordUpdate.put("password",edit_NewPassword.getText().toString());


                    userReference.child(UserID).updateChildren(passwordUpdate).

                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(AccountInformation.this,"Password updated",Toast.LENGTH_LONG ).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AccountInformation.this,"cant update",Toast.LENGTH_LONG ).show();
                                }
                            });

                }


            }
        });
        dialogBuilder.setNegativeButton("CANCEl", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();


    }


    //change username method
    public void showChangeUsernameDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.change_username,null);
        dialogBuilder .setView(dialogview);


        //final EditText edit_OldPassword = (EditText)dialogview.findViewById(R.id.edit_oldpassword);
        final EditText edit_NewUsername = (EditText)dialogview.findViewById(R.id.edit_newUsername);


        dialogBuilder.setTitle("Change Username");
        dialogBuilder.setMessage("Enter information below");

        dialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                userReference = FirebaseDatabase.getInstance().getReference("User");

                if(UserName != null)
                {
                    Map<String,Object> passwordUpdate = new HashMap<>();
                    passwordUpdate.put("userName",edit_NewUsername.getText().toString());


                    userReference.child(UserID).updateChildren(passwordUpdate).

                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(AccountInformation.this,"Password updated",Toast.LENGTH_LONG ).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AccountInformation.this,"cant update",Toast.LENGTH_LONG ).show();
                                }
                            });

                }


            }
        });
        dialogBuilder.setNegativeButton("CANCEl", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();

    }


}
