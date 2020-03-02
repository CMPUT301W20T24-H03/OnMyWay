package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    private DBManager dbManager;
    private EditText fullNameField;
//    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dbManager = DBManager.getInstance();

        TextView userIdLabel = findViewById(R.id.labelUserId);
        TextView userTypeLabel = findViewById(R.id.labelUserType);
        TextView userRatingLabel = findViewById(R.id.labelRating);

        fullNameField = findViewById(R.id.fieldFullName);
        emailField = findViewById(R.id.fieldEmail);
        phoneField = findViewById(R.id.fieldPhone);

        User currentUser = dbManager.getCurrentUser();

        userIdLabel.setText(currentUser.getUserID());
        userTypeLabel.setText(currentUser.isDriver() ? "Driver" : "Rider");
        userRatingLabel.setText(String.valueOf(currentUser.getRating()));

        fullNameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());


        // TODO: Get user profile photo from Gravatar
        // TODO: Get user id and fill label
        // TODO: Get user type and fill label
        // TODO: Get user rating and fill label
    }


    private void showInputErrorMsg(String errorMsg) {
        Log.w(TAG, errorMsg);
        Toast.makeText(EditProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    public void onBackButtonPressed(View view) {
        Log.d(TAG, "Back button pressed");

        super.onBackPressed();
    }


//    public void onLogoutButtonPressed(View view) {
//        Log.d(TAG, "Logout button pressed");
//        dbManager.logoutUser();
//
//        Intent intent = new Intent(EditProfileActivity.this, SplashScreenActivity.class);
//        intent.putExtra("isLoggedOut", true);
//        startActivity(intent);
//    }


    public void onSaveButtonPressed(View view) {
        Log.d(TAG, "Save button pressed");

        CharSequence fullNameChars = fullNameField.getText();
        CharSequence emailAddressChars = emailField.getText();
        CharSequence phoneNumberChars = phoneField.getText();

        InputValidatorResponse fullNameStatus = InputValidator.checkFullName(fullNameChars);
        InputValidatorResponse emailAddressStatus = InputValidator.checkEmail(emailAddressChars);
        InputValidatorResponse phoneStatus = InputValidator.checkPhoneNumber(phoneNumberChars);

        if (!fullNameStatus.success()) {
            showInputErrorMsg(fullNameStatus.getErrorMsg());
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

        // TODO: Reenable this later when User has setPhoneNumber implemented
//        if (phoneStatus.success()) {
//            String formmattedPhoneNumber = phoneStatus.getResult(); // TODO: Use this phone number later to save to database
//        }
//        else {
//            showInputErrorMsg(phoneStatus.getErrorMsg());
//            return;
//        }

        // TODO: Save stuff to database before returning to parent activity
        User currentUser = dbManager.getCurrentUser();

        currentUser.setName(fullNameStatus.getResult());
        currentUser.setEmail(emailAddressStatus.getResult());
//        currentUser.setPhone(phoneStatus.getResult());    // TODO: Need to implement setPhone in User

        Log.d(TAG, "All inputs are valid. Returning to parent activity");
        this.finish();  // Return to parent activity

    }


    public void onDeleteAccountPressed(View view) {
        Log.d(TAG, "Delete Account button pressed");
        // TODO: Show prompt here and call deleteAccount in DBManager (probably?)
    }
}
