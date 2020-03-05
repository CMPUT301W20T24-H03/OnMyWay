package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "OMW/LoginActivity";   // Use this tag for call Log.d()
    private EditText emailField;
    private EditText passwordField;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbManager = new DBManager();

        // Get text from EditTexts
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
    }


    // Ignore presses of the back button
    @Override
    public void onBackPressed() {
       // Literally nothing
    }


    // A helper function to display error messages in console and in a toast
    private void showLoginErrorMsg(String errorMsg) {
        Log.w(TAG, errorMsg);
        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    // Handle logging in user using DBManager
    private void loginUser(String emailAddress, String password) {
        dbManager.setLoginListener(new LoginListener() {
            public void onLoginSuccess() {
                Log.d(TAG, "Authentication successful");

                // Honestly not 100% sure how this works since onLoginSuccess() is never called
                // Maybe this can be moved outside to the method root?
                dbManager.setCurrentUserInfoPulledListener(new CurrentUserInfoPulledListener() {
                    public void onCurrentUserInfoPulled() {
                        Log.d(TAG, "Info for current user pulled successfully");

                        // Check state and either go to rider map or driver map
                        if (State.getCurrentUser().isDriver()) {
                            // Go to DriverMapActivity
                            Log.d(TAG, "Switching to DriverMapActivity");
                            Intent intent = new Intent(LoginActivity.this, DriverMapActivity.class);
                            startActivity(intent);
                        }
                        else {
                            // Go to RiderMapActivity
                            Log.d(TAG, "Switching to RiderMapActivity");
                            Intent intent = new Intent(LoginActivity.this, RiderMapActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }

            public void onLoginFailure(Exception exception) {
                showLoginErrorMsg("Authentication failed. Please check your email and password again" + exception.toString());
            }
        });

        dbManager.loginUser(emailAddress, password, this);
    }


    // Called when login button pressed. Defined in XML
    public void onLoginButtonPressed(View view) {
        CharSequence emailAddressChars = emailField.getText();
        // Make sure the email address is valid
        InputValidatorResponse emailStatus = InputValidator.checkEmail(emailAddressChars);

        if (emailStatus.success()) {
            CharSequence passwordChars = passwordField.getText();
            // Make sure password is valid
            InputValidatorResponse passwordStatus = InputValidator.checkPassword(passwordChars);

            if (passwordStatus.success()) {
                // Pass the email address and password to loginUser if inputs are valid
                loginUser(emailAddressChars.toString(), passwordChars.toString());
            }
            else {
                showLoginErrorMsg(passwordStatus.getErrorMsg());
            }
        }
        else {
            showLoginErrorMsg(emailStatus.getErrorMsg());
        }
    }

    public void onSignUpPressed(View view){
        Intent intent = new Intent(this, SignUpRider.class);
        startActivity(intent);
    }
}
