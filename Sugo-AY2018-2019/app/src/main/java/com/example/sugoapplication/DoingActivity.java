package com.example.sugoapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView do_title, do_taskee, do_dateNeeded, do_amount, bid_desc;
    Button complete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_doing );


        do_title = findViewById( R.id.do_title );
        do_taskee = findViewById( R.id.do_taskee );
        do_dateNeeded = findViewById( R.id.do_dateNeeded );
        do_amount = findViewById( R.id.do_amount );
        bid_desc = findViewById( R.id.bidVie_desc );

        complete_btn = findViewById( R.id.complete_btn );


        complete_btn.setOnClickListener( this );


    }

    @Override
    public void onClick(View v) {
        if(v == complete_btn){
            //add credit payment to current credit
            //confirm payment
            Intent intent = new Intent( this, SidebarActivity.class );
            startActivity(intent);
        }
    }
}
