package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    private DatabaseManager databaseManager;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        databaseManager = new DatabaseManager();

        firstNameField = findViewById(R.id.fieldFirstName);
        lastNameField = findViewById(R.id.fieldLastName);
        emailField = findViewById(R.id.fieldEmail);
        phoneField = findViewById(R.id.fieldPhone);
    }


    private void showInputErrorMsg(String errorMsg) {
        Log.w(TAG, errorMsg);
        Toast.makeText(EditProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    public void onBackButtonPressed(View view) {
        Log.d(TAG, "Back button pressed");
        CharSequence firstNameChars = firstNameField.getText();
        CharSequence lastNameChars = lastNameField.getText();
        CharSequence emailAddressChars = emailField.getText();
        CharSequence phoneNumberChars = phoneField.getText();

        InputValidatorResponse firstNameStatus = InputValidator.checkFirstName(firstNameChars);
        InputValidatorResponse lastNameStatus = InputValidator.checkLastName(lastNameChars);
        InputValidatorResponse emailAddressStatus = InputValidator.checkEmail(emailAddressChars);
        InputValidatorResponse phoneStatus = InputValidator.checkPhoneNumber(phoneNumberChars);

        if (!firstNameStatus.success()) {
            showInputErrorMsg(firstNameStatus.getErrorMsg());
            return;
        }

        if (!lastNameStatus.success()) {
            showInputErrorMsg(lastNameStatus.getErrorMsg());
            return;
        }

        if (!emailAddressStatus.success()) {
            showInputErrorMsg(emailAddressStatus.getErrorMsg());
            return;
        }

        if (!phoneStatus.success()) {
            showInputErrorMsg(phoneStatus.getErrorMsg());
            return;
        }

        if (phoneStatus.success()) {
            String formmattedPhoneNumber = phoneStatus.getResult(); // TODO: Use this phone number later to save to database
        }
        else {
            showInputErrorMsg(phoneStatus.getErrorMsg());
            return;
        }

        // TODO: Save stuff to database before returning to parent activity
        Log.d(TAG, "All inputs are valid. Returning to parent activity");
        this.finish();  // Return to parent activity
    }


    public void onLogoutButtonPressed(View view) {
        Log.d(TAG, "Logout button pressed");
        databaseManager.logoutUser();

        Intent intent = new Intent(EditProfileActivity.this, SplashScreenActivity.class);
        intent.putExtra("isLoggedOut", true);
        startActivity(intent);
    }


    public void onDeleteAccountPressed(View view) {
        Log.d(TAG, "Delete Account button pressed");
        // TODO: Show prompt here and call deleteaccount in DatabaseManager (probably?)
    }
}
