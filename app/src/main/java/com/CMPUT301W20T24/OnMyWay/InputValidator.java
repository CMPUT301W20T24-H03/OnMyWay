package com.CMPUT301W20T24.OnMyWay;

import android.text.TextUtils;
import android.util.Log;
import java.util.regex.Pattern;
import static android.telephony.PhoneNumberUtils.formatNumber;


// Validate various types of text inputs. Look at ResponseStatus() to see how to call it correctly.
public class InputValidator {
    private static final String TAG = "OMW/InputValidator";   // Use this tag for call Log.d()


    /// StackOverflow post by mindriot
    /// Author: https://stackoverflow.com/users/1011746/mindriot
    /// Answer: https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    public static ResponseStatus checkEmail(CharSequence emailAddressChars) {
        Log.d(TAG, "Checking email address");

        if (!TextUtils.isEmpty(emailAddressChars) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressChars).matches()) {
            // Return true if the email address is okay
            return new ResponseStatus(true, emailAddressChars.toString());
        }
        return new ResponseStatus(false, "Email address is not valid");
    }


    // TODO: IMPLEMENT THIS CLASS LATER TO CHECK WHETHER PASSWORDS ARE IN VALID FORM
    public static ResponseStatus checkPassword(CharSequence passwordChars) {
        Log.d(TAG, "Checking password");

        return new ResponseStatus();    // This just returns true for now
    }


    // Helper method to check if first name is valid. Takes a CharSequence from an EditText
    public static ResponseStatus checkFirstName(CharSequence nameChars) {
        // Just call checkName but tell it what type of name so it can print log messages
        return checkName(nameChars, "first");
    }


    // Helper method to check if last name is valid. Takes a CharSequence from an EditText
    public static ResponseStatus checkLastName(CharSequence nameChars) {
        // Just call checkName but tell it what type of name so it can print log messages
        return checkName(nameChars, "last");
    }


    // Method to check if a name is valid. Takes a CharSequence from an EditText.
    // Private because we never call it directly from outside
    private static ResponseStatus checkName(CharSequence nameChars, String nameType) {
        Log.d(TAG, "Checking " + nameType + " name");

        int nameLength = nameChars.length();

        if (nameLength <= 1) {
            return new ResponseStatus(false, "The " + nameType + " name entered is too short");
        }
        else if (nameLength > 40) {
            return new ResponseStatus(false, "The " + nameType + " name entered is too long");
        }
        else if (Pattern.matches("[a-zA-Z]+", nameChars)) {
            // Capitalize the name and return it if the email address is formatted correctly
            return new ResponseStatus(true, Utilities.capitalize(nameChars.toString()));
        }

        return new ResponseStatus(false, "The " + nameType + " name entered is not valid");
    }


    // Method to check if a phone number is valid. Takes a CharSequence from an EditText.
    // If valid, returns a nicely formatted phone number. Use this result

    /// StackOverflow post by Trinimon
    /// Author: https://stackoverflow.com/users/2092587/trinimon
    /// Answer: https://stackoverflow.com/questions/15647327/phone-number-formatting-an-edittext-in-android
    public static ResponseStatus checkPhoneNumber(CharSequence phoneChars) {
        Log.d(TAG, "Checking phone number");

        // Format the phone number nicely, if possible.
        // Locale is fixed to CA to prevent consistency issues
        String formattedPhoneNumber = formatNumber(phoneChars.toString(), "CA");

        if (formattedPhoneNumber != null) {
            // If its a real phone number, return the nicely formatted version
            return new ResponseStatus(true, formattedPhoneNumber);
        }
        else {
            return new ResponseStatus(false, "The phone number entered is not valid");
        }
    }
}
