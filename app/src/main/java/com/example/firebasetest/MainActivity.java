package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void btnlogin(View view) {
        Intent send = new Intent(MainActivity.this, Login.class);
        startActivity(send);
    }

    public void btnsignup(View view) {
        Intent send = new Intent(MainActivity.this, Signup.class);
        startActivity(send);
    }

}