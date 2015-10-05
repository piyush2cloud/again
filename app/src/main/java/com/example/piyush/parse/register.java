package com.example.piyush.parse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;

public class register extends AppCompatActivity
{
    EditText email,user,pwd;
    Button btnReg;
    String getemail,getuser,getpwd;
    String alreadytaken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email  =  (EditText) findViewById(R.id.email);
        pwd    =    (EditText) findViewById(R.id.pwd);
        user   =   (EditText) findViewById(R.id.user);
        btnReg = (Button) findViewById(R.id.btnReg);


    }

    public void onclickbtnReg(View view)
    {
        getemail = email.getText().toString();
        getuser  = user.getText().toString();
        getpwd   = pwd.getText().toString();



        if(getemail.matches("") || getuser.matches("") || getpwd.matches(""))
        {
              fill_all_details_alert();
        }

        else
        {
              createparseuser();
        }
    }



    public void createparseuser()
    {

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null)
        {
             ParseUser.logOut();
        }

        ParseUser user = new ParseUser();

        user.setUsername(getuser);
        user.setPassword(getpwd);
        user.put("email", getemail);


        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(com.parse.ParseException e) {
                if (e == null)
                {
                    Toast.makeText(register.this, "User Added", Toast.LENGTH_SHORT).show();
                    loginpage();
                }

                else
                {
                    alreadytaken = e.getMessage();
                    username_or_password_already_exists_alert();

                }
            }
        });
    }

    private void loginpage()
    {
         Intent i = new Intent(register.this,loginagain.class);
         startActivity(i);
         ParseUser currentUser = ParseUser.getCurrentUser();

         if (currentUser != null)
         {
             ParseObject object = new ParseObject("LocationObject");

             object.put("user",currentUser.getUsername());
             object.put("latitude",40.33);
             object.put("longitude",56.33);
             object.saveInBackground();
             Toast.makeText(register.this,currentUser.getUsername(),Toast.LENGTH_SHORT).show();
         }


    }


    public void fill_all_details_alert()
    {
        new AlertDialog.Builder(register.this)
                .setTitle("FILL ALL DETAILS")
                .setMessage("Please Fill All the Details")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void username_or_password_already_exists_alert()
    {
        new AlertDialog.Builder(register.this)
                .setTitle("Already Exists")
                .setMessage(alreadytaken)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
