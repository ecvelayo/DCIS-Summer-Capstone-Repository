package com.example.sugoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompletionTaskerActivity extends AppCompatActivity {

    ImageView taskee_image;

    public static final String BID_ID = "bid_id";
    public static final String TASK_ID = "task_id";


    TextView taskee_name, completeTaskee_type, completeTaskee_desc, complete_amount, phone_number;


    DatabaseReference databaseBid, databaseTask, databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_completion_tasker );

        taskee_image = findViewById( R.id.taskee_image );

        taskee_name = findViewById( R.id.taskee_name );
        completeTaskee_type = findViewById( R.id.completeTaskee_type );
        completeTaskee_desc = findViewById( R.id.completeTaskee_desc );
        complete_amount = findViewById( R.id.complete_amount );
        phone_number = findViewById( R.id.phone_number );



        databaseTask = FirebaseDatabase.getInstance().getReference( "task" );
        databaseUser = FirebaseDatabase.getInstance().getReference( "users" );
        databaseBid = FirebaseDatabase.getInstance().getReference( "bids" );

        final Intent intent = getIntent();
        final String task_id = intent.getStringExtra( AcceptActivity.TASK_ID );
        databaseTask.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    if(taskSnapshot.child( "task_state" ).getValue().toString().equals( "Completed" ) && taskSnapshot.getKey().equals(task_id)){
                        databaseBid.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot bidSnapshot: dataSnapshot.getChildren()){
                                    if (bidSnapshot.child( "task_id" ).getValue().toString().equals( task_id )){
                                        Intent intent_bid = new Intent( getApplicationContext(), RatingAndReviewActivity.class );
                                        intent_bid.putExtra(BID_ID, bidSnapshot.getKey());
                                        intent_bid.putExtra(TASK_ID, taskSnapshot.child("task_id").getValue().toString());
                                        finish();
                                        startActivity( intent_bid );
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

                        String tasker = taskSnapshot.child( "tasker_name" ).getValue().toString();
                        String task1 = taskSnapshot.child( "task_name" ).getValue().toString();
                        String task_type = taskSnapshot.child( "task_type" ).getValue().toString();
                        String payment1 = taskSnapshot.child( "final_amount" ).getValue().toString();
                        taskee_name.setText( tasker );
                        completeTaskee_type.setText( task_type );
                        completeTaskee_desc.setText( task1 );
                        complete_amount.setText( payment1 );
                        databaseBid.addValueEventListener( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (final DataSnapshot bidSnapshot : dataSnapshot.getChildren()) {
                                    if (bidSnapshot.getKey().equals( task_id )) {
                                        databaseUser.addListenerForSingleValueEvent( new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                                    if (bidSnapshot.child( "user_id" ).getValue().toString().equals( userSnapshot.child( "login_id" ).getValue().toString() )) {
                                                        String phonenumber = userSnapshot.child( "phone_number" ).getValue().toString();


                                                        phone_number.setText( phonenumber );
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

}