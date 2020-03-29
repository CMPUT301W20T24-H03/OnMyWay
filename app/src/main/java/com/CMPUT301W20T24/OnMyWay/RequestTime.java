package com.CMPUT301W20T24.OnMyWay;

import java.util.Calendar;


public class RequestTime {
    private long secondsSinceEpoch;

    // Make an object with the current time if no arguments are given
    public RequestTime() {
        this.secondsSinceEpoch = Calendar.getInstance().getTime().getTime() / 1000;
    }

    public RequestTime(long secondsSinceEpoch) {
        this.secondsSinceEpoch = secondsSinceEpoch;
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
