package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UtilitiesTests {
    @Test
    void testMd5() {
        assertEquals(Utilities.md5("test"), "098f6bcd4621d373cade4e832627b4f6");
        assertEquals(Utilities.md5("driver@example.com"), "8ebb025d54ef4041d0cd7d010d3fc63d");
        assertEquals(Utilities.md5("rider@example.com"), "7043a2172e6454a970ce5c6760bd4a00");
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.md5("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.md5(null);
        });
    }
}
