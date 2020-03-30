package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


/**
 * An activity that allows the user to edit their profile information.
 * Save must be pressed to validate and save the user's entered information.
 * The user can also delete their account from this page
 * @author John
 */
public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "OMW/EditProfileActivity";  // Use this tag for call Log.d()
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneField;
    private ConstraintLayout progressContainer;
    private boolean areAllInputsValid;


    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


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
        ImageView profilePhotoImage = findViewById(R.id.imageCurrentProfilePhoto);
        progressContainer = findViewById(R.id.progressContainer);

        User currentUser = UserRequestState.getCurrentUser();


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
            User currentUser = UserRequestState.getCurrentUser();  // Grab the current user from UserRequestState

            // If all the inputs are valid, update the current user with new values
            currentUser.setFirstName(firstNameStatus.getResult());
            currentUser.setLastName(lastNameStatus.getResult());
            currentUser.setEmail(emailAddressStatus.getResult());
            currentUser.setPhone(phoneStatus.getResult());
            UserRequestState.updateCurrentUser();  // Tell UserRequestState we want to push user changes to FireStore

            Log.d(TAG, "All inputs are valid. Returning to parent activity");
            this.finish();  // Return to parent activity
        }
        else {
            Toast.makeText(EditProfileActivity.this, "Please check your inputs again", Toast.LENGTH_SHORT).show();
        }
    }

    // Called when save button is pressed. Defined in XML. Not implemented yet
    public void onDeleteAccountPressed(View view) {
        Log.d(TAG, "Delete Account button pressed");

        progressContainer.setVisibility(View.VISIBLE);

        DBManager dbManager = new DBManager();

        /// StackOverflow post by David Hedlund
        /// Author: https://stackoverflow.com/users/133802
        /// Answer: https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage(R.string.delete_account_message)
                .setPositiveButton(R.string.text_okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue and delete account
                        Log.d(TAG, "Okay button pressed");

                        dbManager.setUserDeletedListener(new UserDeletedListener() {
                            public void onUserDeleteSuccess() {
                                Intent intent = new Intent(EditProfileActivity.this, SplashScreenActivity.class);
                                intent.putExtra("toastMessage", "Account deleted successfully");
                                startActivity(intent);
                            }

                            public void onUserDeleteFailure() {
                                progressContainer.setVisibility(View.GONE);

                                Toast.makeText(EditProfileActivity.this, "Account deletion failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dbManager.deleteUser();
                    }
                })
                .setNegativeButton(R.string.text_cancel, null)
                .show();
    }
}
