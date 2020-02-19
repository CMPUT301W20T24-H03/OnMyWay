package com.CMPUT301W20T24.OnMyWay;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
    }


    // Ignore presses of the back button
    @Override
    public void onBackPressed() {
       // Literally nothing
    }


    /// StackOverflow post by mindriot
    /// Author: https://stackoverflow.com/users/1011746/mindriot
    /// Answer: https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    private boolean validateEmail(CharSequence emailAddressChars) {
        return !TextUtils.isEmpty(emailAddressChars) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressChars).matches();
    }


    private boolean validatePassword(CharSequence passwordChars) {
        return true; // Inplement this later
    }


    private void checkUserType(FirebaseUser user) {
        Log.d(TAG, "Checking user type");
        Log.d(TAG, user.toString());
        // Check if user is driver or rider here
    }


    private void showLoginError(Exception exception) {
        Log.w(TAG, "signInWithEmail:failure", exception);
        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }



    private void loginUser(String emailAddress, String password) {
        Log.d(TAG, "Logging in user");

        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            checkUserType(mAuth.getCurrentUser());
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            showLoginError(task.getException());
                        }
                    }
                });
    }


    public void onLoginButtonPressed(View view) {
        CharSequence emailAddressChars = emailField.getText();
        CharSequence passwordChars = passwordField.getText();

        if (validateEmail(emailAddressChars)) {
            if (validatePassword(passwordChars)) {
                loginUser(emailAddressChars.toString(), passwordChars.toString());
            }
            else {
                Log.d(TAG, "Password is not valid");
            }
        }
        else {
            Log.d(TAG, "Email address is not valid");
        }
    }
}
