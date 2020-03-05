package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class SignUpRider extends AppCompatActivity {
    //User Inputs
    private EditText emailID;
    private EditText password;
    private EditText firstName;
    private EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_rider);

        emailID = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        firstName = findViewById(R.id.firstnameField);
        lastName = findViewById(R.id.lastnameField);
    }

    public void userSignUp(){
        String userEmail = emailID.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userfirstName = firstName.getText().toString().trim();
        String userlastName = lastName.getText().toString().trim();
    }
    public void onDriverSignUpPressed(){
        //Launch driver sign up page
        //To implement

    }
}
