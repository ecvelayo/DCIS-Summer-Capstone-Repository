package com.example.sugoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SkillsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_skills );

        if(getIntent().hasExtra( "com.example.sugoapplication.skills" )){
            TextView SkillsTextView = (TextView) findViewById( R.id.skillsTextView );
            String text = getIntent().getExtras().getString( "com.example.sugoapplication.skills" );
            SkillsTextView.setText( text );
        }
    }
}
