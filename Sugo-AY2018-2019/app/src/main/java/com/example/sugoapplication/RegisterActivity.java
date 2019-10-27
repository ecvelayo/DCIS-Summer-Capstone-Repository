package com.example.sugoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button reg_btn;
    private EditText Email;
    private EditText Password;
    private EditText dispName;
    private TextView text_reg,confirm_password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //start profiling activity
            finish();
            startActivity(new Intent(getApplicationContext(), SidebarActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        reg_btn = (Button) findViewById(R.id.reg_btn);

        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);
        text_reg = (TextView) findViewById(R.id.text_reg);
        dispName = (EditText) findViewById( R.id.displayName );
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        reg_btn.setOnClickListener(this);
        text_reg.setOnClickListener(this);

    }

    private void registerUser(){
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String confirm_pass = confirm_password.getText().toString().trim();
        final String dName = dispName.getText().toString().trim();

        if(email.isEmpty()){
            //email is empty
            Email.setError("Email is required");
            Email.requestFocus();
            //stopping the function execution
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Please enter a valid email");
            Email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            //password is empty
            Password.setError("Password is required");
            Password.requestFocus();
            //stopping the function execution
            return;
        }
        if(password.length()<6){
            Password.setError("Minimum length for password is 6");
            Password.requestFocus();
            return;
        }
        if(!password.equals(confirm_pass)){
            Password.setError("Password does not match");
            Password.requestFocus();
            return;
        }
        //if validations are ok
        //show progress Dialog
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user has been registered successfully
                            //go to profile activity
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(dName).build();

                            user.updateProfile(profileUpdates);
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Failed to register.. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == reg_btn){
            registerUser();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(view == text_reg){
            //will open LoginActivity
            /*finish();*/
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }


    }
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
