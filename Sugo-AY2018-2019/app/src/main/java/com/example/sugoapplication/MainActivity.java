package com.example.sugoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login_btn;
    private EditText Email;
    private EditText Password;
    private TextView text_log;
    ImageView sugo_pic;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sugo_pic =  findViewById(R.id.sugo_pic);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //start profiling activity
            LogUser logUser = LogUser.getInstance();
            logUser.setFname(firebaseAuth.getCurrentUser().getDisplayName());

            finish();
            startActivity(new Intent(getApplicationContext(), SidebarActivity.class));
        }

        progressDialog = new ProgressDialog(this);


        /*sugo_pic.setImageResource(R.drawable.big_sugo);*/
        login_btn = (Button) findViewById(R.id.login_btn);

        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);

        text_log = (TextView) findViewById(R.id.text_log);

        login_btn.setOnClickListener(this);
        text_log.setOnClickListener(this);
    }
    private void loginUser(){
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }
        //if validations are ok
        //show progress Dialog
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //start profiling activity


                            finish();
                            startActivity(new Intent(getApplicationContext(), SidebarActivity.class));
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if(view == login_btn){
            loginUser();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(view == text_log){
            //will open LoginActivity
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }


    }
}
