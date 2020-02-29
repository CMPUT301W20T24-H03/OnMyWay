package com.CMPUT301W20T24.OnMyWay;

import android.text.TextUtils;

// Validate various types of text inputs. Call InputValidatorResponse() without arguments if the
// check was successful. Otherwise call with the error message
public class InputValidator {
    /// StackOverflow post by mindriot
    /// Author: https://stackoverflow.com/users/1011746/mindriot
    /// Answer: https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    public static InputValidatorResponse checkEmail(CharSequence emailAddressChars) {
        if (!TextUtils.isEmpty(emailAddressChars) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressChars).matches()) {
            return new InputValidatorResponse();
        }
        return new InputValidatorResponse("Email address is not valid");
    }

    // IMPLEMENT THIS CLASS LATER TO CHECK WHETHER PASSWORDS ARE IN VALID FORM
    public static InputValidatorResponse checkPassword(CharSequence passwordChars) {
        return new InputValidatorResponse();
    }
}
