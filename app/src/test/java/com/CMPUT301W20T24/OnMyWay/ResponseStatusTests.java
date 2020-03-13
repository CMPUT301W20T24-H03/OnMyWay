package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests for the ResponseStatus class. The method names here correspond to the ones they are testing
 *
 * @author John
 */
public class ResponseStatusTests {
    @Test
    void testResponseStatus() {
        String testResult = "Result";
        String testError = "Error name";

        // Create different objects
        ResponseStatus successResponse = new ResponseStatus();
        ResponseStatus successWithResultResponse = new ResponseStatus(true, testResult);
        ResponseStatus failureResponse = new ResponseStatus(false, testError);

        // Make sure success() is true on successResponse and false on failureResponse
        assertTrue(successResponse.success());
        assertFalse(failureResponse.success());

        // Make sure getResult() succeeds on successWithResultResponse
        assertEquals(testResult, successWithResultResponse.getResult());

        // Make sure getResult() throws an error on successResponse & failureResponse
        assertThrows(UnsupportedOperationException.class, successResponse::getResult);
        assertThrows(UnsupportedOperationException.class, failureResponse::getResult);

        // Make sure getErrorMsg() succeeds on failureResponse
        assertEquals(testError, failureResponse.getErrorMsg());

        // Make sure getErrorMsg() throws an error on successResponse & successWithResultResponse
        assertThrows(UnsupportedOperationException.class, successResponse::getErrorMsg);
        assertThrows(UnsupportedOperationException.class, successWithResultResponse::getErrorMsg);
    }
}
