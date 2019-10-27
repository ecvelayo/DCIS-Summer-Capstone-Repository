package com.example.sugoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingAndReviewActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseRate, databaseUser, databaseBid, databaseTask;
    private FirebaseAuth firebaseAuth;
    RatingBar ratingBar;
    EditText review_text;
    TextView ratingScale;
    Button submitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_rating_and_review );

        databaseTask = FirebaseDatabase.getInstance().getReference("task");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");
        databaseRate = FirebaseDatabase.getInstance().getReference("ratings");

        ratingBar = findViewById( R.id.ratingBar );
        review_text = findViewById( R.id.review_text );
        ratingScale = findViewById( R.id.ratingScale);
        submitBtn = findViewById( R.id.submitBtn);




        submitBtn.setOnClickListener( this );

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()){
                    case 1:
                        ratingScale.setText("BOGO KA! SCAMMER!");
                        break;
                    case 2:
                        ratingScale.setText("SIGE NALANG!");
                        break;
                    case 3:
                        ratingScale.setText("OK RA!");
                        break;
                    case 4:
                        ratingScale.setText("WAW!");
                        break;
                    case 5:
                        ratingScale.setText("NICE KA BIMBZ!");
                        break;
                     default:
                         ratingScale.setText("");
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        if(v == submitBtn){
            saveRatingandReview();
            finish();
            startActivity(new Intent( getApplicationContext(), SidebarActivity.class ));
        }
    }

    private void saveRatingandReview() {
        final Intent intent = getIntent();
        final String id = databaseRate.push().getKey();
        final String bid_id = intent.getStringExtra(CompletionTaskerActivity.BID_ID);
        final String task_id = intent.getStringExtra(CompletionTaskerActivity.TASK_ID);
        final float rating = ratingBar.getRating();
        final String reviews = review_text.getText().toString().trim();

        databaseBid.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot bidSnapshot: dataSnapshot.getChildren()){
                if (bidSnapshot.getKey().equals( bid_id ) && bidSnapshot.child("task_id").getValue().toString().equals(task_id)){
                    final String login_id = bidSnapshot.child( "user_id" ).getValue().toString();
                    databaseUser.addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                                if (userSnapshot.child( "login_id" ).getValue().toString().equals( login_id )){
                                    Users users = userSnapshot.getValue(Users.class);
                                    String user_id = userSnapshot.child( "user_id" ).getValue().toString();
                                    Rate rate = new Rate(id, reviews, rating,bid_id,user_id);
                                    databaseRate.child(id).setValue(rate);
                                    double total_reviews = (double) users.getTotal_reviews();
                                    double current_rate = (double) users.getCurrent_rate();


                                    current_rate = ((current_rate * total_reviews) + (rating))/(total_reviews+1);

                                    databaseUser.child( user_id ).child( "current_rate" ).setValue( current_rate );
                                    databaseUser.child( user_id ).child( "total_reviews" ).setValue( review_ctr((float) total_reviews) );

                                    databaseBid.removeEventListener(this);
                                    databaseRate.removeEventListener(this);
                                    databaseUser.removeEventListener(this);
                                    break;


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );
                    break;

                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );
    }

    private float review_ctr(float ctr){
        return ctr + 1;
    }

}
