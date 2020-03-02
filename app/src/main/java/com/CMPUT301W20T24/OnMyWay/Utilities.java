package com.CMPUT301W20T24.OnMyWay;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities {
    /// https://stackoverflow.com/questions/4846484/md5-hashing-in-android
    public static String md5(String string) {
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


    static public String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }


    static public String checkStringNotNull(String stringToCheck) {
        if (stringToCheck == null) {
            throw new NullPointerException("The string fetched from FireStore is null. Make sure data is added for this user");
        }
        else {
            return stringToCheck;
        }
    }


    static public boolean checkBooleanNotNull(Boolean booleanToCheck) {
        if (booleanToCheck == null) {
            throw new NullPointerException("The string fetched from FireStore is null. Make sure data is added for this user");
        }
        else {
            return booleanToCheck;    // No unboxing needed. This is cast to a boolean automatically
        }
    }


    static public int checkLongNotNull(Long longToCheck) {
        if (longToCheck == null) {
            throw new NullPointerException("The string fetched from FireStore is null. Make sure data is added for this user");
        }
        else {
            return longToCheck.intValue();
        }
    }
}
