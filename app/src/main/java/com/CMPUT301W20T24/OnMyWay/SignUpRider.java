package com.CMPUT301W20T24.OnMyWay;
/**
 * Class is responsible for handling the sign up procedure for any new rider
 * With an option linking to a new activity for driver account creation
 *
 * Things left to do:
 *  - Add phone number field
 *  - Add additional intent for driver sign up
 *  - Ensure the new user sign-up works
 *  - Ensure that thte user is added to the 'users' collection on firebase
 *
 *  Working on rn:
 *      - Working on database linking with the help of DBManager.pushUserInfo()
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpRider extends AppCompatActivity {
    //User Inputs
    private EditText emailID;
    private EditText password;
    private EditText firstName;
    private EditText lastName;

    private FirebaseAuth mAuth;

    ProgressBar progressBar;

    //instrantiating DBManager()
    DBManager db = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_rider);

        emailID = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        firstName = findViewById(R.id.firstnameField);
        lastName = findViewById(R.id.lastnameField);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    public void onRegisterButtonPressed(View view){
        String userEmail = emailID.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userfirstName = firstName.getText().toString().trim();
        String userlastName = lastName.getText().toString().trim();

        if(userEmail.isEmpty()){
            emailID.setError("Email is Required");
            emailID.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            emailID.setError("Invalid Email");
            emailID.requestFocus();
            return;
        }

        if(userPassword.isEmpty()){
            password.setError("Enter valid password");
            password.requestFocus();
            return;
        }

        if(userPassword.length() < 6){
            password.setError("Minimum length of password should be 6 characters");
            password.requestFocus();
            return;
        }

        if(userfirstName.isEmpty()){
            firstName.setError("First name is Required");
            firstName.requestFocus();
            return;
        }

        if(userlastName.isEmpty()){
            lastName.setError("Last name is Required");
            lastName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        // create new account based on email and password inputted.
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            finish();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null){
                                //get dbmanager to push all the info to firebase
                                User newUser = new User(user, userfirstName, userlastName, false,userEmail, "1231231234", 0,0);
                                db.pushUserInfo(newUser);
                            }
                            startActivity(new Intent(SignUpRider.this, LoginActivity.class));
                        }
                        else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });


    }

    public void onDriverSignUpPressed(){
        //Launch driver sign up page
        //To implement


    }
}
