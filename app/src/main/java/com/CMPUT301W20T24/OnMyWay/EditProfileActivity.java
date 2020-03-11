package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "OMW/EditProfileActivity";  // Use this tag for call Log.d()
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneField;
    private boolean areAllInputsValid;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


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

        userIdLabel.setText(currentUser.getUserId());

        // Check isDriver. If true, set text to "Driver". Otherwise set text to "Rider"
        userTypeLabel.setText(currentUser.isDriver() ? "Driver" : "Rider");
        userRatingLabel.setText(String.valueOf(currentUser.getRating()));

        // Download the profile photo for the current user and display it
        Picasso.get().load(currentUser.getProfilePhotoUrl()).noFade().into(profilePhotoImage);

        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());
    }


    // A helper function to display error messages in console and in a toast
    private void showInputErrorMsg(String errorMsg, EditText fieldWithError) {
        areAllInputsValid = false;

        Log.w(TAG, errorMsg);

        if (fieldWithError != null) {
            fieldWithError.setError(errorMsg);
        }
    }


    // Called when back button is pressed. Defined in XML
    public void onBackButtonPressed(View view) {
        Log.d(TAG, "Back button pressed");

        super.onBackPressed();  // Do whatever the normal back button does
    }


    // Called when save button is pressed. Defined in XML
    public void onSaveButtonPressed(View view) {
        Log.d(TAG, "Save button pressed");

        areAllInputsValid = true;   // Assume all the inputs are valid at the start

        // Get text from EditTexts
        CharSequence firstNameChars = firstNameField.getText();
        CharSequence lastNameChars = lastNameField.getText();
        CharSequence emailAddressChars = emailField.getText();
        CharSequence phoneNumberChars = phoneField.getText();

        // Check if the inputs are valid and store the responses in ResponseStatuses
        ResponseStatus firstNameStatus = InputValidator.checkFirstName(firstNameChars);
        ResponseStatus lastNameStatus = InputValidator.checkLastName(lastNameChars);
        ResponseStatus emailAddressStatus = InputValidator.checkEmail(emailAddressChars);
        ResponseStatus phoneStatus = InputValidator.checkPhoneNumber(phoneNumberChars);

        // If any of the inputs fail validation, show the error message and update UI

        /// StackOverflow post by SilentKiller
        /// Author: https://stackoverflow.com/users/1160282/silentkiller
        /// Answer: https://stackoverflow.com/questions/18225365/show-error-on-the-tip-of-the-edit-text-android
        if (!firstNameStatus.success()) {
            showInputErrorMsg(firstNameStatus.getErrorMsg(), firstNameField);
        }

        if (!lastNameStatus.success()) {
            showInputErrorMsg(lastNameStatus.getErrorMsg(), lastNameField);
        }

        if (!emailAddressStatus.success()) {
            showInputErrorMsg(emailAddressStatus.getErrorMsg(), emailField);
        }

        if (!phoneStatus.success()) {
            showInputErrorMsg(phoneStatus.getErrorMsg(), phoneField);
        }

        if (areAllInputsValid) {
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
        else {
            Toast.makeText(EditProfileActivity.this, "Please check your inputs again", Toast.LENGTH_SHORT).show();
        }
    }

    // Called when save button is pressed. Defined in XML. Not implemented yet
    public void onDeleteAccountPressed(View view) {
        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "Delete Account button pressed");
        // TODO: Show prompt here and call deleteAccount in DBManager (probably?)

    }
}
