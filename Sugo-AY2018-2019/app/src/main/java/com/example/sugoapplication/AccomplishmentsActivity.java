package com.example.sugoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AccomplishmentsActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_accomplishments );
        Intent intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.getUid();

        if(getIntent().hasExtra( ProfileUpdate.ACCOMPLISHMENTS)){
            TextView AccomplishmentsTextView = (TextView) findViewById( R.id.accomplishmentsTextView );
            String text =  intent.getStringExtra(ProfileUpdate.ACCOMPLISHMENTS);
            AccomplishmentsTextView.setText( text );
        }
    }
}
