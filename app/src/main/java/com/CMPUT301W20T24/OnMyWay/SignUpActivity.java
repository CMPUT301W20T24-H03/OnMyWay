package com.CMPUT301W20T24.OnMyWay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class is responsible for handling the sign up procedure for any new rider
 * @author Mahin
 */
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "OMW/SignUpActivity";   // Use this tag for calling Log.d()
    // User Inputs
    private EditText emailID;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private Switch isDriver;
    private boolean driverStatus = false;
    private FirebaseAuth mAuth;

    ConstraintLayout progressContainer;

    //instantiating DBManager()
    DBManager db = new DBManager();


    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailID = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        firstName = findViewById(R.id.firstnameField);
        lastName = findViewById(R.id.lastnameField);
        phoneNumber = findViewById(R.id.phonenumberfield);
        isDriver = findViewById(R.id.user_switch);
        progressContainer = findViewById(R.id.progressContainer);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
    
    public void onRegisterButtonPressed(View view){
        String userEmail = emailID.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userfirstName = firstName.getText().toString().trim();
        String userlastName = lastName.getText().toString().trim();
        String userPhoneNumber = phoneNumber.getText().toString().trim();

        // Error testing for all input fields
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

        if(!isValidPhone(userPhoneNumber)){
            phoneNumber.setError("Phone number is Invalid");
            phoneNumber.requestFocus();
            return;
        }

        if(isDriver.isChecked()){
            driverStatus = true;
        }

        // Throbber to show creation status
        progressContainer.setVisibility(View.VISIBLE);

        // create new account based on email and password inputted.
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            finish();
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null){
                                // Get dbManager to push all the info to firebase
                                User newUser = new User(user.getUid(), userfirstName, userlastName, driverStatus, userEmail, userPhoneNumber, 0,0);                                db.pushUserInfo(newUser);
                            }

                            Intent intent = new Intent(SignUpActivity.this, SplashScreenActivity.class);
                            intent.putExtra("toastMessage", "Account created successfully");
                            startActivity(intent);
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

    /**
     * method is used to valid a user inputted phone number
     * @param phone
     * @return boolean value
     */

    /// Phone number validation from http://tutorialspots.com/android-how-to-check-a-valid-phone-number-2382.html
    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);

        if (matcher.matches()) {
            return true;
        }
        else {
            return false;
        }
    }
}
