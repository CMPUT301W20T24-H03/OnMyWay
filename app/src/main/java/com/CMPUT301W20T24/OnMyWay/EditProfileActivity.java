package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    private DBManager dbManager;
    private EditText fullNameField;
//    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneField;
    private ImageView profilePhotoImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dbManager = new DBManager();

        TextView userIdLabel = findViewById(R.id.labelUserId);
        TextView userTypeLabel = findViewById(R.id.labelUserType);
        TextView userRatingLabel = findViewById(R.id.labelRating);
        fullNameField = findViewById(R.id.fieldFullName);
        emailField = findViewById(R.id.fieldEmail);
        phoneField = findViewById(R.id.fieldPhone);
        profilePhotoImage = findViewById(R.id.imageProfilePhoto);

        User currentUser = State.getCurrentUser();

        userIdLabel.setText(currentUser.getUserID());
        userTypeLabel.setText(currentUser.isDriver() ? "Driver" : "Rider");
        userRatingLabel.setText(String.valueOf(currentUser.getRating()));
        Log.d(TAG, currentUser.getProfilePhotoUrl());   // TODO: Get user profile photo from Gravatar
        Picasso.get().load(currentUser.getProfilePhotoUrl()).into(profilePhotoImage);

        fullNameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());
    }


    private void showInputErrorMsg(String errorMsg) {
        Log.w(TAG, errorMsg);
        Toast.makeText(EditProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    public void onBackButtonPressed(View view) {
        Log.d(TAG, "Back button pressed");

        super.onBackPressed();
    }


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

        if (!phoneStatus.success()) {
            showInputErrorMsg(phoneStatus.getErrorMsg());
            return;
        }

        User currentUser = State.getCurrentUser();

        currentUser.setName(fullNameStatus.getResult());
        currentUser.setEmail(emailAddressStatus.getResult());
        currentUser.setPhone(phoneStatus.getResult());
        State.updateCurrentUser();

        Log.d(TAG, "All inputs are valid. Returning to parent activity");
        this.finish();  // Return to parent activity

    }


    public void onDeleteAccountPressed(View view) {
        Log.d(TAG, "Delete Account button pressed");
        // TODO: Show prompt here and call deleteAccount in DBManager (probably?)
    }
}