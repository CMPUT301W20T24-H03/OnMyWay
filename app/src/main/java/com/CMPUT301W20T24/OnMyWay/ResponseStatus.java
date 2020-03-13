package com.CMPUT301W20T24.OnMyWay;

public class ResponseStatus {
    private boolean status;
    private String errorMsg;
    private String result;


    /**
     * A helpful object for returning status (true, false) as well as either
     * an error messages or result from a method call
     *
     * @author John
     */
    public ResponseStatus(boolean status, String string) {
        if (status) {
            setResult(string);      // If status is true, set the result
        } else {
            setErrorMsg(string);    // Otherwise, set the error message
        }
        this.status = status;        // Set the status to whatever
    }


    // If called with no arguments, we assume the result was successful
    public ResponseStatus() {
        this(true, null);
    }


    // Return whether the call was successful or not
    public boolean success() {
        return status;
    }

    // Return the result of the validation (formatted phone number, capitalized name, etc.)
    public String getResult() {
        if (success()) {
            if (result != null) {
                return result;
            } else {
                // If there is no result but the user calls getResult() anyway, we should throw an error
                throw new UnsupportedOperationException("No result exists for this object. Use the original value instead");
            }
        } else {
            // If we try to get the result of a failed call, we should throw an error
            throw new UnsupportedOperationException("The call to InputValidator failed. No result exists");
        }
    }

    private void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        if (success()) {
            // If we try to get the error message of a successful call, we should throw an error
            throw new UnsupportedOperationException("The call to InputValidator was successful. No error message exists");
        } else {
            return errorMsg;
        }
    }

    private void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
