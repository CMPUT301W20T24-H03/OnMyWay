package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTests {
    @Test
    void testInputValidatorResponse() {
        String errorName = "Error name";
        InputValidatorResponse successResponse = new InputValidatorResponse();
        InputValidatorResponse failureResponse = new InputValidatorResponse(false, errorName);

        assertTrue(successResponse.success());
        assertFalse(failureResponse.success());

        assertEquals(errorName, failureResponse.getErrorMsg());
        assertThrows(UnsupportedOperationException.class, successResponse::getErrorMsg);
    }
}
