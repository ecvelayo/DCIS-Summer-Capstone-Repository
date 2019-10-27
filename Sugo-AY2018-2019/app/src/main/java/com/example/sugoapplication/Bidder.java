package com.example.sugoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bidder extends AppCompatActivity implements View.OnClickListener {

    TextView bidder_name, bidder_skills, completed_task;
    Button accept_btn;
    ListView review_list;
    RatingBar bidder_rate;
    ImageView bidder_image;

    private static final String CHANNNEL_ID = "sugo_app";
    private static final String CHANNEL_NAME = "SuGo Application";
    private static final String CHANNEL_DESC = "SuGo Application Notification";

    public static final String TASK_ID = "task_id";

    DatabaseReference databaseBid,databaseTask,databaseUser,databaseRate;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bidder );

        firebaseAuth = FirebaseAuth.getInstance();
        final String firebaseUser = firebaseAuth.getCurrentUser().getUid();

        databaseTask = FirebaseDatabase.getInstance().getReference("task");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");
        databaseRate = FirebaseDatabase.getInstance().getReference("ratings");


        bidder_name = findViewById( R.id.bidder_name);
        bidder_skills = findViewById( R.id.bidder_skills);
        completed_task = findViewById( R.id.completed_task);

        accept_btn = findViewById( R.id.accept_btn);

        review_list = findViewById( R.id.review_list);

        bidder_rate = findViewById( R.id.bidder_rate);

        bidder_image = findViewById( R.id.bidder_image);


        accept_btn.setOnClickListener( this );

        BidModel bidModel = new BidModel();
        final Intent intent = getIntent();
        final String task_id = intent.getStringExtra( BiddingActivity.TASK_ID );
        final List<String> reviewList = new ArrayList<>(  );
        databaseBid.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bidSnapshot: dataSnapshot.getChildren()){
                    if (bidSnapshot.child( "task_id" ).getValue().equals( task_id )){
                        final String bidder_id = bidSnapshot.child( "user_id" ).getValue().toString();
                        final String task_id = bidSnapshot.child("task_id").getValue().toString();
                        databaseTask.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                                    if (taskSnapshot.child("task_id").getValue().toString().equals(task_id)){
                                        if (!taskSnapshot.child("user_id").getValue().toString().equals(firebaseUser)){
                                            accept_btn.setVisibility(View.GONE);
                                            databaseUser.addValueEventListener( new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                                                        if (userSnapshot.child( "login_id" ).getValue().toString().equals( bidder_id )){
                                                            Users users = userSnapshot.getValue(Users.class);  //retrieve
                                                            float rate =  users.getCurrent_rate();
                                                            bidder_name.setText(users.getFname() );
                                                            bidder_skills.setText( users.getSkills_id() );
                                                            completed_task.setText( "Number of completed tasks: " +  users.getTasks_completed() );
                                                            bidder_rate.setRating(rate);
                                                            if (rate != 0){
                                                                databaseRate.addValueEventListener( new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        for (DataSnapshot rateSnapshot: dataSnapshot.getChildren()){
                                                                            if (rateSnapshot.child( "user_id" ).getValue().toString().equals( bidder_id )){
                                                                                String reviews = rateSnapshot.child( "reviews" ).getValue().toString();
                                                                                reviewList.add( reviews );
                                                                                databaseTask.removeEventListener(this);
                                                                                databaseUser.removeEventListener(this);
                                                                                databaseBid.removeEventListener(this);
                                                                                break;
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
                                                            ArrayAdapter<String> arrayAdapter;
                                                            arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, reviewList );
                                                            review_list.setAdapter(arrayAdapter);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            } );
                                        }else{
                                            databaseUser.addValueEventListener( new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                                                        if (userSnapshot.child( "login_id" ).getValue().toString().equals( bidder_id )){
                                                            Users users = userSnapshot.getValue(Users.class);
                                                            float rate =  users.getCurrent_rate();
                                                            bidder_name.setText(users.getFname() );
                                                            bidder_skills.setText( users.getSkills_id() );
                                                            completed_task.setText( "Number of completed tasks: " +  users.getTasks_completed() );
                                                            bidder_rate.setRating(rate);
                                                            if (rate != 0){
                                                                databaseRate.addValueEventListener( new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        for (DataSnapshot rateSnapshot: dataSnapshot.getChildren()){
                                                                            if (rateSnapshot.child( "user_id" ).getValue().toString().equals( bidder_id )){
                                                                                String reviews = rateSnapshot.child( "reviews" ).getValue().toString();
                                                                                reviewList.add( reviews );
                                                                                databaseTask.removeEventListener(this);
                                                                                databaseUser.removeEventListener(this);
                                                                                databaseBid.removeEventListener(this);
                                                                                break;
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
                                                            ArrayAdapter<String> arrayAdapter;
                                                            arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, reviewList );
                                                            review_list.setAdapter(arrayAdapter);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            } );
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );




    }

    @Override
    public void onClick(View v) {
        if (v == accept_btn){
            Intent intent = getIntent();
            final String task_id = intent.getStringExtra(BiddingActivity.TASK_ID);
            databaseTask.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                        if (taskSnapshot.child("task_id").getValue().toString().equals(task_id)){
                            Intent pass_intent = new Intent( getApplicationContext(), AcceptActivity.class );
                            TaskInserter taskInserter = taskSnapshot.getValue(TaskInserter.class);
                            pass_intent.putExtra( TASK_ID, task_id);
                            finish();
                            startActivity( pass_intent );
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

        }
    }
}
