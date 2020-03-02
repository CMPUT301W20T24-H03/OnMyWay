package com.CMPUT301W20T24.OnMyWay;

import android.text.TextUtils;
import android.util.Log;
import java.util.regex.Pattern;
import static android.telephony.PhoneNumberUtils.formatNumber;


/* Validate various types of text inputs. Call InputValidatorResponse() without arguments if the
check was successful. Otherwise call with the error message */
public class InputValidator {
    private static final String TAG = "OMW/InputValidator";


    /// StackOverflow post by mindriot
    /// Author: https://stackoverflow.com/users/1011746/mindriot
    /// Answer: https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    public static InputValidatorResponse checkEmail(CharSequence emailAddressChars) {
        Log.d(TAG, "Checking email address");

        if (!TextUtils.isEmpty(emailAddressChars) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressChars).matches()) {
            return new InputValidatorResponse(true, emailAddressChars.toString());
        }
        return new InputValidatorResponse(false, "Email address is not valid");
    }


    // TODO: IMPLEMENT THIS CLASS LATER TO CHECK WHETHER PASSWORDS ARE IN VALID FORM
    public static InputValidatorResponse checkPassword(CharSequence passwordChars) {
        Log.d(TAG, "Checking password");

        return new InputValidatorResponse();
    }


    public static InputValidatorResponse checkFirstName(CharSequence nameChars) {
        return checkName(nameChars, "first");
    }


    public static InputValidatorResponse checkLastName(CharSequence nameChars) {
        return checkName(nameChars, "last");
    }


    private static InputValidatorResponse checkName(CharSequence nameChars, String nameType) {
        Log.d(TAG, "Checking " + nameType + " name");

        int nameLength = nameChars.length();

        if (nameLength <= 1) {
            return new InputValidatorResponse(false, "The " + nameType + " name entered is too short");
        }
        else if (nameLength > 40) {
            return new InputValidatorResponse(false, "The " + nameType + " name entered is too long");
        }
        else if (Pattern.matches("[a-zA-Z]+", nameChars)) {
            // Capitalize the name and return it
            return new InputValidatorResponse(true, Utilities.capitalize(nameChars.toString()));
        }

        return new InputValidatorResponse(false, "The " + nameType + " name entered is not valid");
    }


    /// StackOverflow post by Trinimon
    /// Author: https://stackoverflow.com/users/2092587/trinimon
    /// Answer: https://stackoverflow.com/questions/15647327/phone-number-formatting-an-edittext-in-android
    public static InputValidatorResponse checkPhoneNumber(CharSequence phoneChars) {
        Log.d(TAG, "Checking phone number");

        String formattedPhoneNumber = formatNumber(phoneChars.toString(), "CA");

        if (formattedPhoneNumber != null) {
            return new InputValidatorResponse(true, formattedPhoneNumber);
        }
        else {
            return new InputValidatorResponse(false, "The phone number entered is not valid");
        }
    }
}
