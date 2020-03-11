package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class InputValidatorTests {
    @Test
    void testCheckPassword() {
        CharSequence testPassword1 = "password";
        CharSequence testPassword2 = "1234567890";
        CharSequence testPassword3 = "_$&+,:;=?@#|'`<>.^*()%!-~[]{}\\";
        CharSequence testPassword4 = "short";
        CharSequence testPassword5 = "this_is_a_super_long_password_because_it_is_over_40_characters";
        CharSequence testPassword6 = "";
        CharSequence testPassword7 = null;

        assertTrue(InputValidator.checkPassword(testPassword1).success());
        assertTrue(InputValidator.checkPassword(testPassword2).success());
        assertTrue(InputValidator.checkPassword(testPassword3).success());
        assertFalse(InputValidator.checkPassword(testPassword4).success());
        assertFalse(InputValidator.checkPassword(testPassword5).success());
        assertFalse(InputValidator.checkPassword(testPassword6).success());

        Exception exception = assertThrows(NullPointerException.class, () -> {
            InputValidator.checkPassword(testPassword7);
        });
    }


    @Test
    void testCheckFirstName() {
        CharSequence testName1 = "mark";
        CharSequence testName2 = "Anikaa";
        CharSequence testName3 = "john Junior";
        CharSequence testName4 = "Sally_senior";
        CharSequence testName5 = "lalaDeeDa";
        CharSequence testName6 = "1234567890";
        CharSequence testName7 = "_$&+,:;=?@#|'`<>.^*()%!-~[]{}\\";
        CharSequence testName8 = "this_is_a_super_long_name_because_it_is_over_40_characters";
        CharSequence testName9 = "";
        CharSequence testName10 = null;

        ResponseStatus response1 = InputValidator.checkFirstName(testName1);
        ResponseStatus response2 = InputValidator.checkFirstName(testName2);
        ResponseStatus response3 = InputValidator.checkFirstName(testName3);
        ResponseStatus response4 = InputValidator.checkFirstName(testName4);
        ResponseStatus response5 = InputValidator.checkFirstName(testName5);
        ResponseStatus response6 = InputValidator.checkFirstName(testName6);
        ResponseStatus response7 = InputValidator.checkFirstName(testName7);
        ResponseStatus response8 = InputValidator.checkFirstName(testName8);
        ResponseStatus response9 = InputValidator.checkFirstName(testName9);

        assertTrue(response1.success());
        assertTrue(response2.success());
        assertFalse(response3.success());
        assertFalse(response4.success());
        assertTrue(response5.success());
        assertFalse(response6.success());
        assertFalse(response7.success());
        assertFalse(response8.success());
        assertFalse(response9.success());

        Exception exception = assertThrows(NullPointerException.class, () -> {
            InputValidator.checkFirstName(testName10);
        });

        assertEquals("Mark", response1.getResult());
        assertEquals("Anikaa", response2.getResult());
        assertEquals("LalaDeeDa", response5.getResult());
    }


    @Test
    void testCheckLastName() {
        CharSequence testName1 = "mark";
        CharSequence testName2 = "Anikaa";
        CharSequence testName3 = "john Junior";
        CharSequence testName4 = "Sally_senior";
        CharSequence testName5 = "lalaDeeDa";
        CharSequence testName6 = "1234567890";
        CharSequence testName7 = "_$&+,:;=?@#|'`<>.^*()%!-~[]{}\\";
        CharSequence testName8 = "this_is_a_super_long_name_because_it_is_over_40_characters";
        CharSequence testName9 = "";
        CharSequence testName10 = null;

        ResponseStatus response1 = InputValidator.checkLastName(testName1);
        ResponseStatus response2 = InputValidator.checkLastName(testName2);
        ResponseStatus response3 = InputValidator.checkLastName(testName3);
        ResponseStatus response4 = InputValidator.checkLastName(testName4);
        ResponseStatus response5 = InputValidator.checkLastName(testName5);
        ResponseStatus response6 = InputValidator.checkLastName(testName6);
        ResponseStatus response7 = InputValidator.checkLastName(testName7);
        ResponseStatus response8 = InputValidator.checkLastName(testName8);
        ResponseStatus response9 = InputValidator.checkLastName(testName9);

        assertTrue(response1.success());
        assertTrue(response2.success());
        assertFalse(response3.success());
        assertFalse(response4.success());
        assertTrue(response5.success());
        assertFalse(response6.success());
        assertFalse(response7.success());
        assertFalse(response8.success());
        assertFalse(response9.success());

        Exception exception = assertThrows(NullPointerException.class, () -> {
            InputValidator.checkLastName(testName10);
        });

        assertEquals("Mark", response1.getResult());
        assertEquals("Anikaa", response2.getResult());
        assertEquals("LalaDeeDa", response5.getResult());
    }
}
