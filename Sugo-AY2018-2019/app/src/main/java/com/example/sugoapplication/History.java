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

public class History extends Fragment{

    ListView listViewHistory;

    DatabaseReference databaseBid,databaseTask,databaseUser;
    private FirebaseAuth firebaseAuth;


    public static final String TASK_DESC = "task_name";
    public static final String TASK_ID = "task_id";




    View myView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.history, container, false);
        listViewHistory = myView.findViewById( R.id.history_list );

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
        final List<String> historyList = new ArrayList<>();
        databaseTask.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot taskSnapshot: dataSnapshot.getChildren()) {
                    if (taskSnapshot.child("user_id").getValue().equals(firebaseAuth.getCurrentUser().getUid())) {
                        final String task_name = taskSnapshot.child("task_name").getValue().toString();
                        historyList.add(task_name);

                    }
                }
                        listViewHistory.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final Intent intent = new Intent(getContext(), AcceptActivity.class);
                                final String currentTask = parent.getItemAtPosition(position).toString();
                                databaseTask.addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                                            if (taskSnapshot.child( "task_name" ).getValue().toString().equals( currentTask )){
                                                String task_id = taskSnapshot.child( "task_id" ).getValue().toString();
                                                intent.putExtra(TASK_ID,task_id);

                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );

                            }
                        } );

                ArrayAdapter<String> arrayAdapter;
                arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, historyList );
                listViewHistory.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

}
