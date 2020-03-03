package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "OMW/LoginActivity";
    private EditText emailField;
    private EditText passwordField;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbManager = new DBManager();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
    }


    // Ignore presses of the back button
    @Override
    public void onBackPressed() {
       // Literally nothing
    }


    private void showLoginErrorMsg(String errorMsg) {
        Log.w(TAG, errorMsg);
        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    private void loginUser(String emailAddress, String password) {
        dbManager.setLoginListener(new LoginListener() {
            public void onLoginSuccess() {
                Log.d(TAG, "Authentication successful");

                dbManager.setCurrentUserInfoPulledListener(new CurrentUserInfoPulledListener() {
                    public void onCurrentUserInfoPulled() {
                        Log.d(TAG, "Info for current user pulled successfully");

                        // TODO: Check state and either go to rider map or driver map
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


    public void onLoginButtonPressed(View view) {
        CharSequence emailAddressChars = emailField.getText();
        InputValidatorResponse emailStatus = InputValidator.checkEmail(emailAddressChars);

        if (emailStatus.success()) {
            CharSequence passwordChars = passwordField.getText();
            InputValidatorResponse passwordStatus = InputValidator.checkPassword(passwordChars);

            if (passwordStatus.success()) {
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
}
