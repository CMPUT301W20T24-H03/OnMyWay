package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


/**
 * A login page for the user. If the login fails the user is not allowed to continue.
 * Otherwise, the user is redirected to either DriverMapActivity or RiderMapActivity
 * @author John
 */
public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "OMW/LoginActivity";   // Use this tag for call Log.d()
    private EditText emailField;
    private EditText passwordField;
    private DBManager dbManager;
    private boolean areAllInputsValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbManager = new DBManager();

        // Get EditTexts
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
    }


    // HACK TO GO BACK TO THE MAIN ACTIVITY WHEN THE BACK BUTTON IS PRESSED. DISABLE BACK BUTTON LATER
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Switching to MainActivity");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


    // A helper function to display error messages in console and in a toast
//    private void showInputErrorMsg(String errorMsg) {
//        Log.w(TAG, errorMsg);
//        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
//    }


    // A helper function to display error messages in console and in a toast
    private void showInputErrorMsg(String errorMsg, EditText fieldWithError) {
        areAllInputsValid = false;

        Log.w(TAG, errorMsg);
//        Toast.makeText(EditProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();

        if (fieldWithError != null) {
            fieldWithError.setError(errorMsg);
        }
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
                showInputErrorMsg("Authentication failed. Please check your email and password again" + exception.toString(), null);
            }
        });

        dbManager.loginUser(emailAddress, password, this);
    }


    // Called when login button pressed. Defined in XML
/*    public void onLoginButtonPressed(View view) {
        CharSequence emailAddressChars = emailField.getText();
        // Make sure the email address is valid
        ResponseStatus emailStatus = InputValidator.checkEmail(emailAddressChars);

        if (emailStatus.success()) {
            CharSequence passwordChars = passwordField.getText();
            // Make sure password is valid
            ResponseStatus passwordStatus = InputValidator.checkPassword(passwordChars);

            if (passwordStatus.success()) {
                // Pass the email address and password to loginUser if inputs are valid
                loginUser(emailAddressChars.toString(), passwordChars.toString());
            }
            else {
                showInputErrorMsg(passwordStatus.getErrorMsg(), passwordField);
            }
        }
        else {
            showInputErrorMsg(emailStatus.getErrorMsg(), emailField);
        }
    }*/

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
            Toast.makeText(LoginActivity.this, "Please check your inputs again", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSignUpPressed(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
