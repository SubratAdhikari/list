package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.GeoApiContext;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;


public class Dashboard extends AppCompatActivity {

    public static String uid;
    public static double lat;
    public static double lon;

    static TextView lname, lemail, lphone;
    private ImageView profileImg;
    LinearLayout profileLayout;
    Button btnuserlist;


    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Handler gpshandeler = new Handler();

    FusedLocationProviderClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().hide();

        lname = findViewById(R.id.txtname);
        lemail = findViewById(R.id.txtemail);
        lphone = findViewById(R.id.txtphone);
        btnuserlist = findViewById(R.id.btnuserlist);
        profileLayout = findViewById(R.id.profileLayout);

        profileImg = findViewById(R.id.profileImg);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uid = getIntent().getStringExtra("uid");


        client = LocationServices.getFusedLocationProviderClient(this);

        getPermission();

        profileLayout.setVisibility(View.GONE);

        new getUdata().execute();

        btnuserlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlistpage();
            }
        });

    }

    private void userlistpage(){
        Intent send = new Intent(this, UsersPage.class);
        startActivity(send);
    }




    private class getUdata extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            profileLayout.setVisibility(View.VISIBLE);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            DboUserInfo dbuinfo = new DboUserInfo();
            dbuinfo.getuinfo(uid);
            getpimg();
            return null;
        }
    }




    private void getpimg() {
        final int[] done = {0};
        storageReference = FirebaseStorage.getInstance().getReference("profile_pic/"+uid);

        try {
            File localfile = File.createTempFile("temp",".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    profileImg.setImageBitmap(bitmap);
                    profileImg.setScaleX(1);
                    profileImg.setScaleY(1);
                    done[0] =1;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Dashboard.this, "Failed to retrive profile image.", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(done[0]==0){
        //wait
        }
    }

    public void getPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getgpsRunnable.run();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        getPermission();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    public Runnable getgpsRunnable = new Runnable() {
        @Override
        public void run() {
          getmylocation();
          gpshandeler.postDelayed(this,2000);
        }
    };


    public void getmylocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                lat = location.getLatitude();
                lon = location.getLongitude();

                DboUserInfo dbuinfo = new DboUserInfo();
                GioInfo gioInfo = new GioInfo(lat,lon);
                dbuinfo.addGpsCodi(gioInfo, uid);

//                Log.d(TAG, "Lat: "+ lat +", Lon:"+lon);

            }
        });

    }



}