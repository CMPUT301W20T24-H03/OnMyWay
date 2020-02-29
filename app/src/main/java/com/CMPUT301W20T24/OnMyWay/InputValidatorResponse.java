package com.CMPUT301W20T24.OnMyWay;

public class InputValidatorResponse {
    private boolean status;
    private String errorMsg;

    public InputValidatorResponse(boolean status, String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public InputValidatorResponse() {
        this(true, null);
    }

    public InputValidatorResponse(String errorMsg) {
        this(false, errorMsg);

    }

    public boolean success() {
        return status;
    }

    public String getErrorMsg() {
        if (success()) {
            throw new UnsupportedOperationException("The call to InputValidator was successful. No error message exists");
        }
        else {
            return errorMsg;
        }
    }
}
