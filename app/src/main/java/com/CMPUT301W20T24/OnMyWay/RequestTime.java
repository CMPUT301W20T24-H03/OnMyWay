package com.CMPUT301W20T24.OnMyWay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RequestTime {
    private long secondsSinceEpoch;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d', 'y' at 'h':'m", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("a", Locale.US);


    // Make an object with the current time if no arguments are given
    public RequestTime() {
        this.secondsSinceEpoch = calendar.getTime().getTime() / 1000;
    }


    public RequestTime(long secondsSinceEpoch) {
        if (secondsSinceEpoch >= 0) {
            this.secondsSinceEpoch = secondsSinceEpoch;
        }
        else {
            throw new IllegalArgumentException("RequestTime can't take a negative time");
        }
    }


    public long toLong() {
        return secondsSinceEpoch;
    }


    // Get absolute value of the difference between 2 times, formatted like "mm:ss"
    public String getTimeElapsed() {
        long timeDiffSeconds = new RequestTime().toLong() - secondsSinceEpoch;
        long minutes = timeDiffSeconds / 60;
        long seconds = timeDiffSeconds % 60;

        return minutes + ":" + String.format(Locale.US, "%02d", seconds);
    }


    // Get the date as a String, formatted like "April 1, 2020 at 3:46pm"
    public String getDate() {
        calendar.setTimeInMillis(secondsSinceEpoch * 1000);

        return dateFormat.format(calendar.getTime().getTime()) +
                timeFormat.format(calendar.getTime().getTime()).toLowerCase();
    }
}
