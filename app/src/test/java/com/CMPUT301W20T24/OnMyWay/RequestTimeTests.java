package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests for the ResponseStatus class. The method names here correspond to the ones they are testing
 * @author John
 */
public class RequestTimeTests {
    @Test
    void testRequestTime() {
        RequestTime requestTime = new RequestTime(1585777585);

        assertEquals("April 1, 2020 at 3:46pm", requestTime.getDate());

        requestTime = new RequestTime();

        assertEquals(Calendar.getInstance().getTime().getTime() / 1000, requestTime.toLong());
    }
}
