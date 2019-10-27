package com.example.sugoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bids extends Fragment {

    public static final String BID_ID = "bid_id";


    ListView listViewBids;

    DatabaseReference databaseBid,databaseTask,databaseUser;
    private FirebaseAuth firebaseAuth;


    View myView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.bids, container, false);

        listViewBids = myView.findViewById( R.id.bid_list );

        databaseTask = FirebaseDatabase.getInstance().getReference("task");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();





        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        final List<String> bidList = new ArrayList<>();
        databaseBid.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                for (final DataSnapshot bidSnapshot: dataSnapshot.getChildren()) {
                    if (bidSnapshot.child( "user_id" ).getValue().equals( firebaseAuth.getCurrentUser().getUid() )) {
                        final String bid_task_id = bidSnapshot.child( "task_id" ).getValue().toString();
                        databaseTask.addValueEventListener( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                                    if (taskSnapshot.child( "task_id" ).getValue().equals( bid_task_id )){
                                        String task_name = taskSnapshot.child( "task_name" ).getValue().toString();
                                        bidList.add( task_name );

                                        listViewBids.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                final Intent intent = new Intent( getActivity(), BidView.class );
                                                intent.putExtra( BID_ID, parent.getItemAtPosition(position).toString());
                                                Bids.super.onResume();
                                                bidList.clear();


                                                startActivity( intent );
                                            }
                                        } );
                                    }
                                }
                                ArrayAdapter<String> arrayAdapter;
                                arrayAdapter = new ArrayAdapter<>( getActivity(), android.R.layout.simple_list_item_1, bidList );

                                listViewBids.setAdapter(arrayAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );



    }
}
