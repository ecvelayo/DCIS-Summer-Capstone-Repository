package com.example.sugoapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileUpdate extends Fragment implements View.OnClickListener {
    private static final int CHOOSE_IMAGE = 101;
    Button profile_btn;
    TextView first_name, last_name, phone_number, course, id_number,skills,verify;
    ImageView profile_image;
    RatingBar profile_rate;
    ListView reviews;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUser,databaseRate;
    public static final String SKILLS = "Skills";
    public static final String ACCOMPLISHMENTS = "Accomplishments";

    String profileImageUrl;
    Uri uriProfileImage;
    private ProgressBar progressBar;

    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate( R.layout.profile, container, false );

        Intent intent = new Intent();

        Bitmap bitmap = (Bitmap) intent.getParcelableExtra( "Bitmap" );

        firebaseAuth = FirebaseAuth.getInstance();

        databaseUser = FirebaseDatabase.getInstance().getReference( "users" );
        databaseRate = FirebaseDatabase.getInstance().getReference("ratings");
        profile_btn = (Button) myView.findViewById( R.id.save_profile );

        profile_image = (ImageView) myView.findViewById( R.id.profile_image );

        profile_rate = myView.findViewById( R.id.profile_rate );

        reviews = myView.findViewById( R.id.reviews );

        verify = myView.findViewById(R.id.verify);
        skills = myView.findViewById( R.id.skills );
        first_name = (TextView) myView.findViewById( R.id.first_name );
        last_name = (TextView) myView.findViewById( R.id.last_name );
        phone_number = (TextView) myView.findViewById( R.id.phone_number );
        course = (TextView) myView.findViewById( R.id.course );
        id_number = (TextView) myView.findViewById( R.id.id_number );

        profile_image = myView.findViewById( R.id.image_id );
//        profile_image.setImageBitmap( bitmap );


        reviews.setOnItemClickListener( null );
        profile_btn.setOnClickListener( this );
//        profile_image.setOnClickListener( this );

        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        final List<String> reviewList = new ArrayList<>(  );
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseUser.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (firebaseAuth.getCurrentUser().getUid().equals( userSnapshot.child( "login_id" ).getValue() )) {
                        Users users = userSnapshot.getValue(Users.class);
                        double rate = (double) users.getCurrent_rate();
                        first_name.setText( users.getFname() );
                        last_name.setText( users.getLname() );
                        phone_number.setText( users.getPhone_number() );
                        course.setText( users.getCourse() );
                        id_number.setText( users.getId_number() );
                        skills.setText( users.getSkills_id() );
                        profile_rate.setRating((float) rate);
                        if (rate != 0){
                            databaseRate.addValueEventListener( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot rateSnapshot: dataSnapshot.getChildren()){
                                        if (rateSnapshot.child( "user_id" ).getValue().toString().equals( firebaseAuth.getCurrentUser().getUid() )){
                                            Rate rate = rateSnapshot.getValue(Rate.class);
                                            String reviews = rate.getReview();
                                            reviewList.add( reviews );
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            } );
                        }else{
                            reviewList.add( "No reviews yet" );

                        }
                        if(user.isEmailVerified()){
                            verify.setText("Email Verified");
                        }else{
                            verify.setText("Email is not yet Verified(Click to Verify)");
                            verify.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Verification Email Sent",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }


                        ArrayAdapter<String> arrayAdapter;
                        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, reviewList );
                        reviews.setAdapter(arrayAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap( getActivity().getContentResolver(), uriProfileImage );
                profile_image.setImageBitmap( bitmap );

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebaseStorage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference( "profilepics/" + System.currentTimeMillis() + ".jpg" );

        if (uriProfileImage != null) {
            progressBar.setVisibility( View.VISIBLE );
            storageReference.putFile( uriProfileImage ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility( View.GONE );
                    profileImageUrl = uriProfileImage.toString();
                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility( View.GONE );
                    Toast.makeText( getContext(), "", Toast.LENGTH_SHORT ).show();
                }
            } );
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent, "Select Profile Picture" ), CHOOSE_IMAGE );

    }

    @Override
    public void onClick(View v) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (v == profile_image) {
            showImageChooser();
            startActivity( new Intent( getContext(), SidebarActivity.class ) );
        }
    }

}