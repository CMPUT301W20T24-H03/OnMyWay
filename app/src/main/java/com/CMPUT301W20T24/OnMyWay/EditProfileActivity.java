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
    private static final String TAG = "OMW/EditProfileActivity";  // Use this tag for call Log.d()
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView userIdLabel = findViewById(R.id.labelUserId);
        TextView userTypeLabel = findViewById(R.id.labelUserType);
        TextView userRatingLabel = findViewById(R.id.labelRating);
        firstNameField = findViewById(R.id.fieldFirstName);
        lastNameField = findViewById(R.id.fieldLastName);
        emailField = findViewById(R.id.fieldEmail);
        phoneField = findViewById(R.id.fieldPhone);
        ImageView profilePhotoImage = findViewById(R.id.imageProfilePhoto);

        User currentUser = State.getCurrentUser();

        userIdLabel.setText(currentUser.getUserID());

        // Check isDriver. If true, set text to "Driver". Otherwise set text to "Rider"
        userTypeLabel.setText(currentUser.isDriver() ? "Driver" : "Rider");
        userRatingLabel.setText(String.valueOf(currentUser.getRating()));

        // Download the profile photo for the current user and display it
        Picasso.get().load(currentUser.getProfilePhotoUrl()).into(profilePhotoImage);

        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());
    }


    // A helper function to display error messages in console and in a toast
    private void showInputErrorMsg(String errorMsg) {
        Log.w(TAG, errorMsg);
        Toast.makeText(EditProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    // Called when back button is pressed. Defined in XML
    public void onBackButtonPressed(View view) {
        Log.d(TAG, "Back button pressed");

        super.onBackPressed();  // Do whatever the normal back button does
    }


    // Called when save button is pressed. Defined in XML
    public void onSaveButtonPressed(View view) {
        Log.d(TAG, "Save button pressed");

        // Get text from EditTexts
        CharSequence firstNameChars = firstNameField.getText();
        CharSequence lastNameChars = lastNameField.getText();
        CharSequence emailAddressChars = emailField.getText();
        CharSequence phoneNumberChars = phoneField.getText();

        // Check if the inputs are valid and store the responses in InputValidatorResponses
        InputValidatorResponse firstNameStatus = InputValidator.checkFirstName(firstNameChars);
        InputValidatorResponse lastNameStatus = InputValidator.checkLastName(lastNameChars);
        InputValidatorResponse emailAddressStatus = InputValidator.checkEmail(emailAddressChars);
        InputValidatorResponse phoneStatus = InputValidator.checkPhoneNumber(phoneNumberChars);

        // If any of the inputs fail validation, show the error message and exit the method
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

        if (!phoneStatus.success()) {
            showInputErrorMsg(phoneStatus.getErrorMsg());
            return;
        }

        User currentUser = State.getCurrentUser();  // Grab the current user from State

        // If all the inputs are valid, update the current user with new values
        currentUser.setFirstName(firstNameStatus.getResult());
        currentUser.setLastName(lastNameStatus.getResult());
        currentUser.setEmail(emailAddressStatus.getResult());
        currentUser.setPhone(phoneStatus.getResult());
        State.updateCurrentUser();  // Tell State we want to push user changes to FireStore

        Log.d(TAG, "All inputs are valid. Returning to parent activity");
        this.finish();  // Return to parent activity
    }

    // Called when save button is pressed. Defined in XML. Not implemented yet
    public void onDeleteAccountPressed(View view) {
        Log.d(TAG, "Delete Account button pressed");
        // TODO: Show prompt here and call deleteAccount in DBManager (probably?)
    }
}
