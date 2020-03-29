package com.CMPUT301W20T24.OnMyWay;

import java.util.Calendar;

import static java.lang.Math.abs;

public class RequestTime {
    private long secondsSinceEpoch;

    public RequestTime() {
        secondsSinceEpoch = Calendar.getInstance().getTime().getTime() / 1000;
    }

    public long toLong() {
        return secondsSinceEpoch;
    }

    // Get absolute value of the difference between 2 times, formatted like mm:ss
    public String getTimeElapsed() {
        long timeDiffSeconds = new RequestTime().toLong() - secondsSinceEpoch;
        long minutes = timeDiffSeconds / 60;
        long seconds = timeDiffSeconds % 60;

        return minutes + ":" + seconds;
    }
}
