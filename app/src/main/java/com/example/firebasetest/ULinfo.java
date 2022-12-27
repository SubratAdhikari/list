package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Log;

public class ULinfo {

    String userID, name, phone, distance, duration, imageurl;
    double durationDo;

    public ULinfo(String userID, String name, String phone, String distance, String duration, String imageurl) {


        this.durationDo = Double.parseDouble(duration.substring(0,duration.length() - 4));

        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.distance = distance;
        this.duration = duration;
        this.imageurl = imageurl;
    }


}
