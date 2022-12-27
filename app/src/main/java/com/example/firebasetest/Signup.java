package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText edtsemail, edtspassword, edtscpassword, edtsname, edtsnumber, edtsage;
    String userid;

    private ImageView profilePic;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        edtsemail = findViewById(R.id.edtsemail);
        edtspassword = findViewById(R.id.edtspassword);
        edtscpassword = findViewById(R.id.edtscpassword);
        edtsname = findViewById(R.id.edtsname);
        edtsnumber = findViewById(R.id.edtsnumber);
        edtsage = findViewById(R.id.edtsage);

        profilePic = findViewById(R.id.profilePic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });


    }


    public void btnssignup(View view) {
        String stemail = edtsemail.getText().toString();
        String stpassword = edtspassword.getText().toString();
        String stcpassword = edtscpassword.getText().toString();

        if ( stcpassword.equals(stpassword) ){
            mAuth.createUserWithEmailAndPassword(stemail, stpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                userid = user.getUid();
                                Log.d(TAG, "signup uid: "+ userid);
                                Toast.makeText(Signup.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                uploadPicture();
                                insertuserinfo();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Signup.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "password and conform password does not match.", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertuserinfo()
    {
        DboUserInfo dbuinfo = new DboUserInfo();
        UserInfo userInfo =new UserInfo(edtsname.getText().toString(),edtsnumber.getText().toString(),edtsage.getText().toString(),edtsemail.getText().toString());
        dbuinfo.adduinfo(userInfo, userid);
        Intent send = new Intent(Signup.this, MainActivity.class);
        startActivity(send);

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            profilePic.setScaleX(1);
            profilePic.setScaleY(1);

        }
    }

    private void uploadPicture() {

        StorageReference uprofileImagesRef = storageReference.child("profile_pic/"+userid);
        uprofileImagesRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Signup.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });

    }
}