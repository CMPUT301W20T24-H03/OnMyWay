package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DriverActivityTests class. The method names here correspond to the ones they are testing
 *
 */

public class DriverActivityTests {
    @Test
    public void TestgeoDist (){
        double lat1 = 53.546;
        double lat2 = 53.54;
        double lat3 = -53.546;
        double lat4 = 50.53;
        double lat5 = -50.53;
        double lat6 = 53.55;
        double lon1 = -113.49;
        double lon2 = -113.51;
        double lon3 = 113.49;
        double lon4 = -110;
        double lon5 = 108.53;
        double lon6 = -113.4;

        DriverViewRequestsActivity driverViewRequestsActivity = new DriverViewRequestsActivity();
        // within 22 km trivial case check. lat lon slightly lower
        assertTrue(driverViewRequestsActivity.geoDist(lat1,lon1,lat2,lon2));
        // within 22 km trivial case check. lat lon slightly higher
        assertTrue(driverViewRequestsActivity.geoDist(lat1,lon1,lat6,lon6));
        // if both are negative of original check
        assertFalse(driverViewRequestsActivity.geoDist(lat1,lon1,lat3,lon3));
        // far away but lat lon are of same postitive/negative sign
        assertFalse(driverViewRequestsActivity.geoDist(lat1,lon1,lat4,lon4));
        // far away and opposite sign
        assertFalse(driverViewRequestsActivity.geoDist(lat1,lon1,lat5,lon5));

    }

}


