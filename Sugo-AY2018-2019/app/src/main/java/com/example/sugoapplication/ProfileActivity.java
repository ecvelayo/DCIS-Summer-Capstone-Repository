package com.example.sugoapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CHOOSE_IMAGE = 101;
    public static final String USER_ID = "user_id";

    private FirebaseAuth firebaseAuth;
    private ImageView image_id;
    private ProgressBar progressBar;
    private EditText first_name, last_name, id_number, phone_number, course, editTextSkills;
    Users users = new Users();
    DatabaseReference databaseUsers;
    String profileImageUrl;
    Uri uriProfileImage;

    private Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent( getApplicationContext(), MainActivity.class );
            databaseUsers.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DatabaseReference fname = databaseUsers.child( "users" ).child("fname");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

            finish();
            startActivity(intent);
        }

        image_id = (ImageView) findViewById(R.id.image_id);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        course = (EditText) findViewById(R.id.course);
        id_number = (EditText) findViewById(R.id.id_number);
        phone_number = (EditText) findViewById(R.id.phone_number);
        editTextSkills = (EditText) findViewById(R.id.editTextSkills);
        continue_btn = (Button) findViewById(R.id.continue_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        continue_btn.setOnClickListener(this);
        image_id.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                image_id.setImageBitmap(bitmap);
                image_id.setDrawingCacheEnabled(true);
                Bitmap b = image_id.getDrawingCache();
                Intent intent = new Intent();

                intent.putExtra( "Bitmap", b );


                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebaseStorage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis() +".png");

        if(uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            storageReference.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageUrl = uriProfileImage.toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);

    }



    @Override
    public void onClick(View v) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (v == image_id) {
            showImageChooser();
        }
        if (v == continue_btn) {
            saveUserInformation();
        }
    }

    private void saveUserInformation() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String email = firebaseUser.getEmail();
        String fname = first_name.getText().toString().trim();
        String lname = last_name.getText().toString().trim();
        String idNum = id_number.getText().toString().trim();
        String phone = phone_number.getText().toString().trim();
        String Course = course.getText().toString().trim();
        String skills = editTextSkills.getText().toString().trim();
        String login = firebaseUser.getUid();

        if(!TextUtils.isEmpty(fname)){

            String id = databaseUsers.push().getKey();

            Users users = new Users(id, email, fname, lname, Course, idNum, phone,login, skills );
            Intent intent = new Intent( getApplicationContext(), SidebarActivity.class );
            intent.putExtra( USER_ID,  users.getLogin_id());


            databaseUsers.child(id).setValue(users);

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }

    }

}
