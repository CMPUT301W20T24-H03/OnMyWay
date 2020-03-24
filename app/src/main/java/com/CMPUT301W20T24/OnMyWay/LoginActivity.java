package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * A login page for the user. If the login fails the user is not allowed to continue.
 * Otherwise, the user is redirected to either DriverMapActivity or RiderMapActivity
 * @author John
 */
public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "OMW/LoginActivity";   // Use this tag for calling Log.d()
    private EditText emailField;
    private EditText passwordField;
    private ConstraintLayout progressContainer;
    private OnlineDBManager onlineDbManager;
    private boolean areAllInputsValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onlineDbManager = new OnlineDBManager();

        // Get EditTexts
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        progressContainer = findViewById(R.id.progressContainer);
    }


    // Disable back button for this activity
    @Override
    public void onBackPressed() {
        // Literally nothing
    }


    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    // A helper function to display error messages in console and in a toast
    private void showInputErrorMsg(String errorMsg, EditText fieldWithError) {
        areAllInputsValid = false;

        Log.w(TAG, errorMsg);

        if (fieldWithError == null) {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
        else {
            fieldWithError.setError(errorMsg);
        }
    }


    // Handle logging in user using OnlineDBManager
    private void loginUser(String emailAddress, String password) {
        onlineDbManager.setLoginListener(new LoginListener() {
            public void onLoginSuccess() {
                Log.d(TAG, "Authentication successful");

                // Honestly not 100% sure how this works since onLoginSuccess() is never called
                // Maybe this can be moved outside to the method root?
                onlineDbManager.setCurrentUserInfoPulledListener(new CurrentUserInfoPulledListener() {
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
                progressContainer.setVisibility(View.GONE);

                if (exception == null) {
                    showInputErrorMsg("Authentication failed. Please check your email and password again", null);
                }
                else {
                    showInputErrorMsg(exception.getMessage(), null);
                }
            }
        });

        progressContainer.setVisibility(View.VISIBLE);
        onlineDbManager.loginUser(emailAddress, password, this);
    }


    public void onLoginButtonPressed(View view) {
        Log.d(TAG, "Login button pressed");

        areAllInputsValid = true;   // Assume all the inputs are valid at the start

        // Get text from EditTexts
        CharSequence emailAddressChars = emailField.getText();
        CharSequence passwordChars = passwordField.getText();

        // If any of the inputs fail validation, show the error message and update UI

        // Check if the inputs are valid and store the responses in ResponseStatuses
        ResponseStatus emailStatus = InputValidator.checkEmail(emailAddressChars);
        ResponseStatus passwordStatus = InputValidator.checkPassword(passwordChars);

        /// StackOverflow post by SilentKiller
        /// Author: https://stackoverflow.com/users/1160282/silentkiller
        /// Answer: https://stackoverflow.com/questions/18225365/show-error-on-the-tip-of-the-edit-text-android
        if (!emailStatus.success()) {
            showInputErrorMsg(emailStatus.getErrorMsg(), emailField);
        }

        if (!passwordStatus.success()) {
            showInputErrorMsg(passwordStatus.getErrorMsg(), passwordField);
        }

        if (areAllInputsValid) {
            Log.d(TAG, "All inputs are valid. Trying to login now");

            // Pass the email address and password to loginUser if inputs are valid
            loginUser(emailAddressChars.toString(), passwordChars.toString());
        }
        else {
            showInputErrorMsg("Authentication failed. Please check your email and password again", null);
        }
    }

    public void onSignUpPressed(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
