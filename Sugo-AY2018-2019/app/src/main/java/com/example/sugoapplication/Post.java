package com.example.sugoapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Post extends AppCompatActivity implements View.OnClickListener {

    public static final String TASK_TYPE = "task_type";
    public static final String TASK_DESC = "task_name";
    public static final String TASKEE_NAME = "taskee_name";
    public static final String STARTING_AMOUNT = "starting_amount";
    public static final String DATE_NEEDED = "date_needed";
    public static final String TASK_STATE = "task_state";
    public static final String TASK_ID = "task_id";
    public static final String USER_ID = "user_id";

    private Button post_btn, date_btn;
    private EditText desc, starting_amount;
    DatePickerDialog datePickerDialog;
    Spinner spinner;
    int year, month, day;
    Calendar calendar;
    private DatabaseReference databaseTask, databaseUser;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_post );
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        desc = (EditText) findViewById(R.id.description);
        starting_amount = (EditText) findViewById(R.id.amount);

        databaseTask = FirebaseDatabase.getInstance().getReference("task");


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        post_btn = (Button) findViewById(R.id.post_btn);
        date_btn = (Button) findViewById(R.id.date_btn);

        spinner = findViewById(R.id.spinner_post);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        post_btn.setOnClickListener(this);
        date_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if(v == date_btn) {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(Post.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    date_btn.setText(day + "/" + month + "/" + year);
                    date_btn.setText(day + "/" + (month + 1) + "/" + year);
                }
            }, year, month, day);
            datePickerDialog.show();
        }else if(v == post_btn){
            saveTaskInformation();
        }
    }

    private void saveTaskInformation(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String date = date_btn.getText().toString().trim();
        String spin_type = spinner.getSelectedItem().toString();
        String start = starting_amount.getText().toString().trim();
        String task = desc.getText().toString().trim();
        String user_id = firebaseUser.getUid();
        String taskee_name = firebaseUser.getDisplayName();

        if(!TextUtils.isEmpty(start)){

            String id = databaseTask.push().getKey();
            TaskInserter insertTask = new TaskInserter(user_id,id,taskee_name,spin_type, task,"Pending", date, start,"null");
            databaseTask.child(id).setValue(insertTask);


            Toast.makeText(this, "Task posted", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, SidebarActivity.class));
        }else{
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
        }


    }
}
