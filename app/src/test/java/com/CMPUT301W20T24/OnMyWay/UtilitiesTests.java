package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests for the Utilities class. The method names here correspond to the ones they are testing
 * @author John
 */
public class UtilitiesTests {
    @Test
    void testMd5() {
        assertEquals("098f6bcd4621d373cade4e832627b4f6", Utilities.md5("test"));
        assertEquals("8ebb025d54ef4041d0cd7d010d3fc63d", Utilities.md5("driver@example.com"));
        assertEquals("7043a2172e6454a970ce5c6760bd4a00", Utilities.md5("rider@example.com"));
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.md5("");  // Empty string should throw exception
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.md5(null);  // Null string should throw exception
        });
    }

    @Test
    void testCapitalize() {
        assertEquals("Test", Utilities.capitalize("test"));
        assertEquals("An", Utilities.capitalize("an"));
        assertEquals("A", Utilities.capitalize("a"));
        assertEquals("Mr. john", Utilities.capitalize("mr. john"));
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.capitalize("");  // Empty string should throw exception
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.capitalize(null);  // Null string should throw exception
        });
    }

    @Test
    void testCheckStringNotNull() {
        assertEquals("test", Utilities.checkStringNotNull("test"));
        assertEquals("an", Utilities.checkStringNotNull("an"));
        assertEquals("a", Utilities.checkStringNotNull("a"));
        assertEquals("mr. john", Utilities.checkStringNotNull("mr. john"));
        assertEquals("", Utilities.checkStringNotNull(""));
        assertThrows(NullPointerException.class, () -> {
            Utilities.checkStringNotNull(null);  // Null string should throw exception
        });
    }

    @Test
    void testCheckBooleanNotNull() {
        Boolean true1 = true;
        boolean true2 = true;
        Boolean false1 = false;
        boolean false2 = false;

        assertTrue(Utilities.checkBooleanNotNull(true1));
        assertTrue(Utilities.checkBooleanNotNull(true2));
        assertFalse(Utilities.checkBooleanNotNull(false1));
        assertFalse(Utilities.checkBooleanNotNull(false2));
        assertThrows(NullPointerException.class, () -> {
            Utilities.checkBooleanNotNull(null);  // Null string should throw exception
        });
    }

    @Test
    void testCheckLongNotNull() {
        Long long1 = 0L;
        Long long2 = -1000L;
        Long long3 = 99999999L;

        assertEquals(0, Utilities.checkLongNotNull(long1));
        assertEquals(-1000, Utilities.checkLongNotNull(long2));
        assertEquals(99999999, Utilities.checkLongNotNull(long3));
        assertThrows(NullPointerException.class, () -> {
            Utilities.checkLongNotNull(null);  // Null string should throw exception
        });
    }
}
