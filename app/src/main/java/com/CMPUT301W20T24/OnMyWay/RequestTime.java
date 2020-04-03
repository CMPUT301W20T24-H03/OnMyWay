package com.CMPUT301W20T24.OnMyWay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A class that stores a date/time for a request
 * @author John
 */
public class RequestTime {
    private long secondsSinceEpoch; // Stores the time in this format
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d', 'y' at 'h':'m", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("a", Locale.US);


    /**
     * Default constructor for RequestTime. Make an object with the current time if no arguments are given
     * @author John
     */
    public RequestTime() {
        this.secondsSinceEpoch = calendar.getTime().getTime() / 1000;
    }


    /**
     * Constructor for RequestTime. Make an object representing a specific time, given a time in
     * secondsSinceEpoch format
     * @param secondsSinceEpoch A time stored in the format, seconds since Jan 1, 1970
     * @author John
     */
    public RequestTime(long secondsSinceEpoch) {
        if (secondsSinceEpoch >= 0) {
            this.secondsSinceEpoch = secondsSinceEpoch;
        }
        else {
            throw new IllegalArgumentException("RequestTime can't take a negative time");
        }
    }


    /**
     * Returns a representation of the date/time stored as a long
     * @return A representation of the date/time stored as a long
     * @author John
     */
    public long toLong() {
        return secondsSinceEpoch;
    }



    /**
     * Returns the difference between the current time and the time stored in the RequestTime object.
     * The result is a string formatted like like "mm:ss"
     * @return A string representation of the time elapsed in the format, "mm:ss"
     * @author John
     */
    public String getTimeElapsed() {
        long timeDiffSeconds = new RequestTime().toLong() - secondsSinceEpoch;
        long minutes = timeDiffSeconds / 60;
        long seconds = timeDiffSeconds % 60;

        return minutes + ":" + String.format(Locale.US, "%02d", seconds);
    }


    /**
     * Returns the stored date/time as a String, formatted like "April 1, 2020 at 3:46pm"
     * @return The date/time as a String, in the format, "April 1, 2020 at 3:46pm"
     * @author John
     */
    //
    public String getDate() {
        calendar.setTimeInMillis(secondsSinceEpoch * 1000);

        return dateFormat.format(calendar.getTime().getTime()) +
                timeFormat.format(calendar.getTime().getTime()).toLowerCase();
    }
}
