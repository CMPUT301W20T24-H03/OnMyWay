package com.CMPUT301W20T24.OnMyWay;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utilities {
    private static final String TAG = "OMW/Utilities";   // Use this tag for call Log.d()


    /// StackOverflow post by Den Delimarsky
    /// Author: https://stackoverflow.com/users/303696/den-delimarsky
    /// Answer: https://stackoverflow.com/questions/4846484/md5-hashing-in-android
    public static String md5(String string) {
        if (string == "" || string == null) {
            throw new IllegalArgumentException("Getting the hash of an empty string makes no sense");
        }
        else {
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(string.getBytes());
                byte[] messageDigest = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest) {
                    String hexDigit = Integer.toHexString(0xFF & aMessageDigest);
                    while (hexDigit.length() < 2)
                        hexDigit = "0" + hexDigit;
                    hexString.append(hexDigit);
                }
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }
    }


    // Capitalize the first letter of a string
    static public String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }


    // Check if a string is null and throw an error if so
    static public String checkStringNotNull(String stringToCheck) {
        if (stringToCheck == null) {
            throw new NullPointerException("The string fetched from FireStore is null. Make sure data is added for this user");
        }
        else {
            return stringToCheck;
        }
    }


    // Check if a Boolean object is null and return the value if not
    static public boolean checkBooleanNotNull(Boolean booleanToCheck) {
        if (booleanToCheck == null) {
            throw new NullPointerException("The string fetched from FireStore is null. Make sure data is added for this user");
        }
        else {
            return booleanToCheck;    // No unboxing needed. This is cast to a boolean automatically
        }
    }


    // CHeck if a Long object is null and return the value as an int if not
    static public int checkLongNotNull(Long longToCheck) {
        if (longToCheck == null) {
            throw new NullPointerException("The string fetched from FireStore is null. Make sure data is added for this user");
        }
        else {
            return longToCheck.intValue();
        }
    }
}
