package com.example.sugoapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BidView extends AppCompatActivity {

    TextView bid_title,bid_desc,current_bid,bid_start,bid_date,bid_state;

    public static final String TASK_ID = "task_id";

    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseBid,databaseTask,databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bid_view );

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseTask = FirebaseDatabase.getInstance().getReference("task");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");

        bid_title = findViewById( R.id.bidView_title );
        bid_desc = findViewById( R.id.bidView_desc );
        bid_start = findViewById( R.id.bidView_start );
        bid_state = findViewById( R.id.bidView_state );
        current_bid = findViewById( R.id.current_bidView );
        bid_date = findViewById( R.id.bidView_date );

        Intent intent = getIntent();

        final String bid_id = intent.getStringExtra( Bids.BID_ID);

        databaseTask.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                if (taskSnapshot.child("task_name").getValue().toString().equals( bid_id )){
                    bid_title.setText( taskSnapshot.child( "task_type" ).getValue().toString() );
                    bid_desc.setText( taskSnapshot.child( "task_name" ).getValue().toString() );
                    bid_start.setText( taskSnapshot.child( "starting_amount" ).getValue().toString() );
                    bid_state.setText( taskSnapshot.child( "task_state"  ).getValue().toString());
                    current_bid.setText( taskSnapshot.child( "final_amount" ).getValue().toString() );
                    bid_date.setText( taskSnapshot.child( "date_needed" ).getValue().toString() );
                    if (taskSnapshot.child("task_state").getValue().toString().equals("Accepted")){
                        Intent intent_bid = new Intent(getApplicationContext(),CompletionTaskeeActivity.class);
                        intent_bid.putExtra( TASK_ID, taskSnapshot.child( "task_id" ).getValue().toString());
                        finish();
                        startActivity(intent_bid);
                    }
                }
            }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }



}
