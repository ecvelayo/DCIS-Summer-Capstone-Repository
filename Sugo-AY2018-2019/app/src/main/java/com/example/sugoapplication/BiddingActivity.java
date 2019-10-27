package com.example.sugoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BiddingActivity extends AppCompatActivity{

    public static final String BID_AMOUNT = "bid_amount";
    public static final String TASKER_NAME = "tasker_name";
    public static final String USER_ID = "user_id";
    public static final String TASK_ID = "task_id";

    ListView bidding_list;

    List<BidModel> bidder_list;
    BidModel bidModel = new BidModel();

    TextView textViewTaskType;
    TextView textViewTaskDesc;
    TextView textViewTaskStartingAmount;
    TextView textViewTaskDateNeeded;
    TextView textViewTaskTaskee;


    EditText editTextBidAmount;

    Button buttonBid;

    DatabaseReference databaseBid,databaseTask,databaseUser;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bidding );

        databaseTask = FirebaseDatabase.getInstance().getReference("task");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        final String userID = firebaseUser.getUid();

        bidding_list = findViewById( R.id.bidding_list );

        textViewTaskTaskee = findViewById( R.id.bid_taskee );
        textViewTaskStartingAmount = findViewById( R.id.bid_start );
        textViewTaskType = findViewById( R.id.bidView_title );
        textViewTaskDateNeeded = findViewById( R.id.bid_dateNeeded );
        textViewTaskDesc = findViewById( R.id.bidView_desc );

        editTextBidAmount = findViewById( R.id.bidAmount );

        buttonBid = findViewById( R.id.bid_btn );

        Intent intent = getIntent();

        String type = intent.getStringExtra( SidebarActivity.TASK_TYPE );
        String desc = intent.getStringExtra( SidebarActivity.TASK_DESC );
        String date_needed = intent.getStringExtra( SidebarActivity.DATE_NEEDED );
        String taskee = intent.getStringExtra( SidebarActivity.TASKEE_NAME );
        String starting = intent.getStringExtra( SidebarActivity.STARTING_AMOUNT );
        final String task_id = intent.getStringExtra( SidebarActivity.TASK_ID );
        textViewTaskType.setText( type );
        textViewTaskDesc.setText( desc );
        textViewTaskDateNeeded.setText( date_needed );
        textViewTaskTaskee.setText( taskee );
        textViewTaskStartingAmount.setText( starting );
        buttonBid.setVisibility(View.GONE);
        editTextBidAmount.setVisibility(View.GONE);


        bidder_list = new ArrayList<>();
        BiddingActivity.super.onResume();
        bidder_list.clear();
        databaseBid.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                for (DataSnapshot bidSnapshot: dataSnapshot.getChildren()) {
                    BidModel bidModel = bidSnapshot.getValue( BidModel.class );
                    if (bidSnapshot.child( "task_id" ).getValue().toString().equals( task_id ) ) {
                        bidder_list.add( bidModel );
                    }
                }
                BidderView taskAdapter = new BidderView( BiddingActivity.this, bidder_list );
                bidding_list.setAdapter( taskAdapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        bidding_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Bidder.class);
                BidModel bidModel = bidder_list.get(position);
                BiddingActivity.super.onResume();
                bidder_list.clear();

                intent.putExtra(TASKER_NAME, bidModel.getTasker_name());
                intent.putExtra(TASK_ID, bidModel.getTask_id());

                startActivity(intent);
            }
        });




        databaseTask.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    if (taskSnapshot.getKey().equals( task_id ) && !taskSnapshot.child("user_id").getValue().toString().equals(firebaseUser.getUid())) {
                        if (taskSnapshot.child( "tasker_name" ).getValue().toString().equals( "null" )) {
                            buttonBid.setVisibility(View.VISIBLE);
                            editTextBidAmount.setVisibility(View.VISIBLE);
                        }else{
                            buttonBid.setVisibility(View.VISIBLE);
                            editTextBidAmount.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                startActivity( new Intent( getApplicationContext() , SidebarActivity.class) );
            }
        } );



        databaseBid = FirebaseDatabase.getInstance().getReference("bids").child(task_id);


        buttonBid.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputBid();


            }
        } );

    }

    private void inputBid(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String bidAmount = editTextBidAmount.getText().toString().trim();
        LogUser logUser = LogUser.getInstance();

        Intent intent = getIntent();
        final String task_id = intent.getStringExtra( SidebarActivity.TASK_ID );
        String user_id = firebaseUser.getUid();
        String taskee_name = intent.getStringExtra( SidebarActivity.TASKEE_NAME );
        String tasker_name = firebaseUser.getDisplayName();


        if(!TextUtils.isEmpty( bidAmount )){
            String id = databaseBid.push().getKey();
            databaseTask.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (final DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                        if(taskSnapshot.getKey().equals( task_id ) ){
                            databaseBid.addValueEventListener( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    databaseTask.child( task_id ).child( "tasker_name" ).setValue(dataSnapshot.child( "tasker_name" ).getValue());
                                    databaseTask.child( task_id ).child( "tasker_id" ).setValue( dataSnapshot.child( "tasker_id" ).getValue() );
                                    databaseTask.child( task_id ).child( "final_amount" ).setValue( dataSnapshot.child( "bid_amount" ).getValue() );
                                    databaseTask.removeEventListener(this);
                                    databaseBid.removeEventListener(this);
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

            BidModel bidModel = new BidModel( id, task_id, user_id, bidAmount, tasker_name, taskee_name);
            databaseBid.setValue( bidModel );
            Toast.makeText( this, "Bid successful", Toast.LENGTH_SHORT ).show();
            finish();
            startActivity(new Intent( getApplicationContext(), SidebarActivity.class ));
        }else{
            Toast.makeText( this, "Enter bid Amount", Toast.LENGTH_SHORT ).show();
        }



    }


}
