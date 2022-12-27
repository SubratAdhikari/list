package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Bookingpage extends AppCompatActivity {

    TextView nameTV,phoneTV,distanceTV,durationTV;
    ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage);

        nameTV = findViewById(R.id.txtname);
        phoneTV = findViewById(R.id.txtphone);
        distanceTV = findViewById(R.id.txtdistance);
        durationTV = findViewById(R.id.txtduration);
        profileImg = findViewById(R.id.profileImg);

        Intent intent = this.getIntent();
        if (intent != null){

            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String distance = intent.getStringExtra("distance");
            String duration = intent.getStringExtra("duration");
            String imageurl = intent.getStringExtra("imageurl");
            nameTV.setText(name);
            phoneTV.setText(phone);
            distanceTV.setText(distance);
            durationTV.setText(duration);
            Glide.with(this).load(imageurl).into(profileImg);
            profileImg.setScaleX(1);
            profileImg.setScaleY(1);


        }

    }



}