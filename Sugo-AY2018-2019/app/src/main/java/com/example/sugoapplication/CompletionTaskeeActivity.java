package com.example.sugoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompletionTaskeeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TASK_ID = "task_id";
    public static final String USER_ID = "user_id";

    ImageView tasker_image;

    TextView tasker_name, tasker_number, task_name, payment;

    Button complete_btn;

    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseBid,databaseTask,databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_completion_taskee );

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseTask = FirebaseDatabase.getInstance().getReference("task");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");


        task_name = findViewById( R.id.task_name );
        tasker_name = findViewById( R.id.tasker_name );
        tasker_number = findViewById( R.id.tasker_number );
        payment =findViewById( R.id.payment );

        tasker_image = findViewById( R.id.tasker_image );

        complete_btn = findViewById( R.id.complete_btn );


        final Intent intent = getIntent();
        final String task_id = intent.getStringExtra( BidView.TASK_ID );
        databaseTask.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                    if (taskSnapshot.getKey().equals( task_id )){
                        String tasker = taskSnapshot.child("taskee_name").getValue().toString();
                        String task1 = taskSnapshot.child("task_name").getValue().toString();
                        String payment1 = taskSnapshot.child("final_amount").getValue().toString();
                        intent.putExtra(USER_ID,taskSnapshot.child("user_id").getValue().toString());
                        intent.putExtra(TASK_ID,taskSnapshot.child("task_id").getValue().toString());
                        tasker_name.setText(tasker);
                        task_name.setText(task1);
                        payment.setText(payment1);
                        databaseBid.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(final DataSnapshot bidSnapshot: dataSnapshot.getChildren()){
                                    if(bidSnapshot.getKey().equals(task_id)){
                                        databaseUser.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){

                                               if(bidSnapshot.child("user_id").getValue().toString().equals(userSnapshot.child("login_id").getValue().toString())){
                                                   String phonenumber = userSnapshot.child("phone_number").getValue().toString();


                                                   tasker_number.setText(phonenumber);
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
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        complete_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final Intent intent = getIntent();
        final String task_id = intent.getStringExtra( AcceptActivity.TASK_ID );
        if(v == complete_btn){

            databaseTask.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                        TaskInserter taskInserter = taskSnapshot.getValue(TaskInserter.class);
                        if(taskInserter.getTask_state().equals("Accepted") && taskSnapshot.getKey().equals(task_id)){
                            databaseTask.child(task_id).child("task_state").setValue("Completed");
                            final String tasks_id = taskSnapshot.child( "task_id" ).getValue().toString();
                            databaseTask.removeEventListener(this);
                            databaseBid.addListenerForSingleValueEvent( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot bidSnapshot: dataSnapshot.getChildren()){
                                    if (bidSnapshot.child( "task_id" ).getValue().toString().equals( tasks_id )){
                                        final String login_id = bidSnapshot.child( "user_id" ).getValue().toString();
                                        databaseBid.removeEventListener(this);
                                        databaseUser.addListenerForSingleValueEvent( new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                                                    if (userSnapshot.child( "login_id" ).getValue().toString().equals( login_id )){
                                                        String user_id = userSnapshot.child( "user_id" ).getValue().toString();
                                                        long completed_ctr = (long) userSnapshot.child( "tasks_completed" ).getValue();
                                                        databaseUser.child( user_id ).child( "tasks_completed" ).setValue( completed_ctr + 1);
                                                        finish();
                                                        startActivity(new Intent(getApplicationContext(),SidebarActivity.class));

                                                        databaseUser.removeEventListener(this);

                                                        break;

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
            });


        }
    }

}
