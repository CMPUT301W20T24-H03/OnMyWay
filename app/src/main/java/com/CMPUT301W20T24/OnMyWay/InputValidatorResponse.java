package com.CMPUT301W20T24.OnMyWay;

public class InputValidatorResponse {
    private boolean status;
    private String errorMsg;
    private String result;


    public InputValidatorResponse(boolean status, String string) {
        if (status) {
            setResult(string);
        }
        else {
            setErrorMsg(string);
        }
        this.status = status;
    }


    public InputValidatorResponse() {
        this(true, null);
    }


//    public InputValidatorResponse(String errorMsg) {
//        this(false, errorMsg);
//
//    }


    public boolean success() {
        return status;
    }


    private void setResult(String result) {
        this.result = result;
    }


    public String getResult() {
        if (success()) {
            if (result != null) {
                return result;
            }
            else {
                throw new UnsupportedOperationException("No result exists for this object. Use the original value instead");
            }
        }
        else {
            throw new UnsupportedOperationException("The call to InputValidator failed. No result exists");
        }
    }


    private void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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
