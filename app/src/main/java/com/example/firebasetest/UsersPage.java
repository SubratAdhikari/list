package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.firebasetest.databinding.ActivityUsersPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class UsersPage extends AppCompatActivity {

    public DatabaseReference databaseReference, databaseReference2;
    private FirebaseStorage storage2;
    private StorageReference storageReference2;



    ActivityUsersPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //listView = findViewById(R.id.listview);

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        databaseReference = db.getReference("UserInfo");
        databaseReference2 = db.getReference();

        storage2 = FirebaseStorage.getInstance();
        storageReference2 = storage2.getReference();

        new getUdata2().execute();

    }


    private class getUdata2 extends AsyncTask<String,String,ULinfo[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ULinfo[] userarraylist) {
            displayy(userarraylist);
            super.onPostExecute(userarraylist);
        }

        @Override
        protected ULinfo[] doInBackground(String... strings) {

            return getusersdata2();
        }
    }



    private ULinfo[] getusersdata2(){
        final int[] done = {0};
        final int[] gotdt = {0};
        final int[] len = {10};
        final String[] tempuid = {""};
        Log.d(TAG, "info101  "+"001");
        ArrayList<String> userid = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> phone = new ArrayList<>();
        ArrayList<String> lat = new ArrayList<>();
        ArrayList<String> lon = new ArrayList<>();
        ArrayList<String> imgurl = new ArrayList<>();
        ArrayList<String> distancel = new ArrayList<>();
        ArrayList<String> durationl = new ArrayList<>();

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {


                int i=0;

                for (DataSnapshot snapshot1 : snapshot.child("UserInfo").getChildren()) {

                    tempuid[0]= snapshot1.getKey().toString();

                    if (tempuid[0].equals(Dashboard.uid) || snapshot.child("UserInfo").child(tempuid[0]).child("onoffduty").getValue().equals("0")) {

//                        Log.d(TAG, "info101 *********** " + snapshot1.getKey().toString() + " ---- " + Dashboard.uid);
                    }
                    else
                    {

                        userid.add(snapshot1.getKey().toString());


                        if (snapshot.child("UserInfo").child(userid.get(i)).child("name").getValue() != null) {
                            name.add(snapshot.child("UserInfo").child(userid.get(i)).child("name").getValue().toString());

                        }

                        if (snapshot.child("UserInfo").child(userid.get(i)).child("phone_Number").getValue() != null) {
                            phone.add(snapshot.child("UserInfo").child(userid.get(i)).child("phone_Number").getValue().toString());

                        }

                        if (snapshot.child("GioLocations").child(userid.get(i)).child("lat").getValue() != null) {
                            lat.add(snapshot.child("GioLocations").child(userid.get(i)).child("lat").getValue().toString());

                        }

                        if (snapshot.child("GioLocations").child(userid.get(i)).child("lon").getValue() != null) {
                            lon.add(snapshot.child("GioLocations").child(userid.get(i)).child("lon").getValue().toString());

                        }


                        double Olat = Dashboard.lat;
                        double Olon = Dashboard.lon;



                        RequestQueue requestQueue = Volley.newRequestQueue(UsersPage.this);
                        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                                .buildUpon()
                                .appendQueryParameter("destination", lat.get(i)+","+lon.get(i))
                                .appendQueryParameter("origin", Olat+","+Olon )
                                .appendQueryParameter("mode", "driving")
                                .appendQueryParameter("key", "AIzaSyA3D501HtQiKik_DTHQxSQ3E9sHzTyoOP0")
                                .toString();

                        distancel.add("0 ****");
                        durationl.add("0 ****");
                        int finalI1 = i;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    String status = response.getString("status");


                                    if (status.equals("OK")) {

                                        JSONArray routes = response.getJSONArray("routes");
                                        JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");

                                        JSONObject distanceObj = (JSONObject) legs.getJSONObject(0).get("distance");
                                        String distance = distanceObj.getString("text");

                                        JSONObject durationObj = (JSONObject) legs.getJSONObject(0).get("duration");
                                        String duration = durationObj.getString("text");

                                        Log.d(TAG, "rrrrrrrrr "+ "Distance "+ distance +" Duration " + duration);


                                        distancel.set(finalI1,distance);
                                        durationl.set(finalI1,duration);
                                        gotdt[0] = gotdt[0] + 1;


                                    }
                                }  catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        RetryPolicy retryPolicy = new DefaultRetryPolicy(500, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsonObjectRequest.setRetryPolicy(retryPolicy);
                        requestQueue.add(jsonObjectRequest);


                        imgurl.add("https://www.apple.com/ac/structured-data/images/knowledge_graph_logo.png?202208021258");
                        int finalI = i;
                        storageReference2.child("profile_pic/" + userid.get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imgurl.set(finalI, uri.toString());
                                done[0] = done[0] + 1;

                                if (done[0] == 3) {
                                    Log.d(TAG, "info101 7 " + imgurl);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                        i = userid.size();
                        len[0] = userid.size();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        while (done[0]<len[0]) {

        }
        while (gotdt[0]<len[0]){

        }

        Log.d(TAG, "info101 200 "+ userid);
        Log.d(TAG, "info101 300 "+ name);
        Log.d(TAG, "info101 400 "+ phone);
        Log.d(TAG, "info101 500 "+ lat);
        Log.d(TAG, "info101 600 "+ lon);

        Log.d(TAG, "info101 700 "+ imgurl);

        ULinfo[] userarraylist=new ULinfo[len[0]];

        for (int j = 0; j< name.size();j++){

            ULinfo ulistinfo = new ULinfo(userid.get(j),name.get(j),phone.get(j),distancel.get(j), durationl.get(j), imgurl.get(j));

            userarraylist[j]=ulistinfo;
        }


        Arrays.sort(userarraylist, (a, b) -> Double.compare(a.durationDo,b.durationDo));


       return userarraylist;

    }


    void displayy(ULinfo[] userarraylist ){

        UlistViewAdapter ulistViewAdapter = new UlistViewAdapter(UsersPage.this,userarraylist);


        binding.listview.setAdapter(ulistViewAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent bookingP = new Intent(UsersPage.this,Bookingpage.class);
                bookingP.putExtra("name", userarraylist[i].name);
                bookingP.putExtra("phone", userarraylist[i].phone);
                bookingP.putExtra("distance", userarraylist[i].distance);
                bookingP.putExtra("duration", userarraylist[i].duration);
                bookingP.putExtra("imageurl", userarraylist[i].imageurl);

                startActivity(bookingP);
            }
        });

    }


}