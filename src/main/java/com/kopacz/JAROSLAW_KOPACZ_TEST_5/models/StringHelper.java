package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

public class StringHelper {
    public static String firstCharToLowerCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

}
