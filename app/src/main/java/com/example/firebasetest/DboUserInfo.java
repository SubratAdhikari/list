package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DboUserInfo {

    public DatabaseReference databaseReference, databaseReference2;

    public DatabaseReference gpsCReference;

    public DboUserInfo() {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("UserInfo");

        databaseReference2 = db.getReference();

        gpsCReference = db.getReference("GioLocations");


    }

    public Task<Void> adduinfo(UserInfo uinfo, String uid)
    {
        return databaseReference.child(uid).setValue(uinfo);
    }

    public void addGpsCodi(GioInfo gioInfo,String uid)
    {
        gpsCReference.child(uid).setValue(gioInfo);
    }

    public void getuinfo(String uid)
    {

        final int[] done = {0};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                String name="",email="",phone="";

                if (snapshot.child(uid).child("name").getValue() != null) {
                    name = snapshot.child(uid).child("name").getValue().toString();
                    Dashboard.lname.setText(name);
                }

                if (snapshot.child(uid).child("email").getValue() != null) {
                    email = snapshot.child(uid).child("email").getValue().toString();
                    Dashboard.lemail.setText(email);
                }
                if (snapshot.child(uid).child("phone_Number").getValue() != null) {
                    phone = snapshot.child(uid).child("phone_Number").getValue().toString();
                    Dashboard.lphone.setText(phone);
                }
                done[0] =1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        while(done[0]==0){
            //wait
        }

    }




}
