package com.example.piyush.parse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

import java.text.ParseException;

public class login extends AppCompatActivity
{

    EditText userfield,pwdfield;
    Button login,register;
    String loginerror;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, "APypapLDSnBOlyfsaIHPhR4dHx04C8kGfkhnRlra", "z7n7asHROPAIn47a25P28yQSna6t8RWuXuU319Ux");




        userfield = (EditText) findViewById(R.id.userfield);
        pwdfield  =  (EditText) findViewById(R.id.pwdfield);
        login     = (Button) findViewById(R.id.btnLogin);
        register  =  (Button) findViewById(R.id.btnregister);



        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null)
        {
            userfield.setText(currentUser.getUsername());
            ParseUser.logOut();
        }
        else
        {
            userfield.setText("");
        }
    }

    public void getlogin(View view)
    {

        ParseUser.logInInBackground(userfield.getText().toString(), pwdfield.getText().toString(), new LogInCallback() {

            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {

                if (parseUser != null) {
                    Toast.makeText(login.this, "welcome", Toast.LENGTH_SHORT).show();
                    gotomainactivity();
                } else {
                    loginerror = e.getMessage();
                    Toast.makeText(login.this, loginerror,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getregister(View view)
    {
        Intent i = new Intent(login.this,register.class);
        startActivity(i);
    }

    private void gotomainactivity()
    {
        Intent i = new Intent(login.this,MainActivity.class);
        startActivity(i);
    }



}
