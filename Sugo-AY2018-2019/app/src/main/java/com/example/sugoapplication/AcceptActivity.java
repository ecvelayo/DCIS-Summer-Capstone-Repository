package com.example.sugoapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptActivity extends AppCompatActivity implements View.OnClickListener {

    TextView accept_title,accept_desc,accept_curr,accept_start,accept_bidder,accept_date,task_state,accept_start2,accept_curr2,accept_date2;
    Button accept_btn,cancel_btn;

    public static final String USER_ID = "task_id";
    public static final String TASK_ID = "task_id";
    public static final String BID_ID = "bid_id";

    private static final String CHANNNEL_ID = "sugo_app";
    private static final String CHANNEL_NAME = "SuGo Application";
    private static final String CHANNEL_DESC = "SuGo Application Notification";


    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseBid,databaseTask,databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_accept );

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseTask = FirebaseDatabase.getInstance().getReference("task");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");

        accept_title = findViewById( R.id.accept_title );
        accept_desc = findViewById( R.id.accept_desc );
        accept_start = findViewById( R.id.accept_start );
        accept_bidder = findViewById( R.id.accept_bidder );
        accept_curr = findViewById( R.id.accept_curr );
        task_state = findViewById(R.id.task_state);
        accept_btn = findViewById( R.id.accept_btn );
        cancel_btn = findViewById( R.id.cancel_btn );
        accept_date = findViewById( R.id.accept_date );
        accept_start2 = findViewById( R.id.accept_start2 );
        accept_curr2 = findViewById( R.id.accept_curr2 );
        accept_date2 = findViewById( R.id.accept_date2 );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        final Intent intent = getIntent();

        final String taskcatch = intent.getStringExtra( History.TASK_ID );
        final String bidcatch = intent.getStringExtra( Bidder.TASK_ID);
        databaseTask.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                    if (taskSnapshot.child("task_id").getValue().toString().equals( taskcatch ) || taskSnapshot.child( "task_id" ).getValue().toString().equals( bidcatch )){
                        String type = taskSnapshot.child( "task_type" ).getValue().toString();
                        String desc = taskSnapshot.child( "task_name" ).getValue().toString();
                        String date_needed = taskSnapshot.child( "date_needed" ).getValue().toString();
                        String curr = taskSnapshot.child( "final_amount" ).getValue().toString();
                        String starting = taskSnapshot.child( "starting_amount" ).getValue().toString();
                        String tasker_name = taskSnapshot.child( "tasker_name" ).getValue().toString();
                        String state = taskSnapshot.child("task_state").getValue().toString();

                        accept_title.setText( type );
                        accept_desc.setText( desc );
                        accept_date.setText( date_needed );
                        accept_curr.setText( curr );
                        accept_start.setText( starting );
                        accept_bidder.setText( tasker_name );
                        task_state.setText(state);
                        if(taskSnapshot.child( "task_state" ).getValue().equals( "Accepted" ) ){
                            accept_btn.setText("View Progress");
                        }
                        if(taskSnapshot.child( "task_state" ).getValue().equals( "Canceled" )|| taskSnapshot.child("task_state").getValue().equals("Completed") ){
                            accept_btn.setVisibility( View.GONE );
                            cancel_btn.setVisibility( View.GONE );
                            accept_start2.setVisibility( View.GONE);
                            accept_start.setVisibility( View.GONE );
                            accept_curr2.setText( "Paid Amount:" );
                            accept_date2.setText( "Date Completed:" );



                        }
                        break;

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        accept_btn.setOnClickListener( this );
        cancel_btn.setOnClickListener( this );



    }

    @Override
    public void onClick(View v) {
        final Intent intent = getIntent();
        final String task_id = intent.getStringExtra( History.TASK_DESC );
        final String taskBidder_id = intent.getStringExtra( Bidder.TASK_ID );

        if(v == accept_btn){
            //Send notification via sms
            databaseTask.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                            if (taskSnapshot.child("task_id").getValue().toString().equals(taskBidder_id) && taskSnapshot.child("task_state").getValue().toString().equals("Pending")) {

                                String key = taskSnapshot.getKey();
                                databaseTask.child(taskBidder_id).child("task_state").setValue("Accepted");
                                databaseBid.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot bidSnapshot: dataSnapshot.getChildren()){
                                            if (bidSnapshot.child("user_id").getValue().toString().equals(firebaseAuth.getUid())){
                                                displayNotification();

                                                Intent intent_pass = new Intent(getApplicationContext(), CompletionTaskerActivity.class);
                                                intent_pass.putExtra(TASK_ID, taskBidder_id);
                                                databaseTask.removeEventListener(this);
                                                databaseBid.removeEventListener(this);
                                                finish();
                                                startActivity(intent_pass);

                                            }else{

                                                Intent intent_pass = new Intent(getApplicationContext(), CompletionTaskerActivity.class);
                                                intent_pass.putExtra(TASK_ID, taskBidder_id);
                                                databaseTask.removeEventListener(this);
                                                databaseBid.removeEventListener(this);
                                                finish();
                                                startActivity(intent_pass);

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                break;
                            } else {
                                String key = taskSnapshot.getKey();

                                databaseTask.removeEventListener(this);
                                Intent intent_pass = new Intent(getApplicationContext(), CompletionTaskerActivity.class);
                                intent_pass.putExtra(TASK_ID, taskBidder_id);
                                finish();
                                startActivity(intent_pass);
                            }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            } );
            //change task state to accepted

        }
        if(v == cancel_btn) {
            //change task state to canceled
            databaseTask.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        if (taskSnapshot.getKey().equals(taskBidder_id)) {

                            databaseTask.child(taskBidder_id).child("task_state").setValue("Canceled");
                            databaseTask.removeEventListener(this);
                            finish();
                            startActivity(new Intent(getApplicationContext(), SidebarActivity.class));
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

    private void displayNotification(){
        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(this, CHANNNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Your bid has been accepted!")
                        .setContentText("Do your task")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat nNotificationMgr = NotificationManagerCompat.from(this);
        nNotificationMgr.notify(1,nBuilder.build());

    }
}
