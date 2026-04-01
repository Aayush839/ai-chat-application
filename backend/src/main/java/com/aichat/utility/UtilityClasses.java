package com.aichat.utility;

import org.antlr.v4.runtime.misc.NotNull;

public class UtilityClasses {

    /**
     * Converts String to int.
     * Throws NumberFormatException if string is invalid.
     */
    public static int toInt(String str) {
        if (str == null) return 0;
        return Integer.parseInt(str);
    }

    public static Long getLong(String str) {
        if (str == null) return 0l;
        return Long.parseLong(str);
    }

    /**
     * Converts String to int safely.
     * Returns the defaultValue if the string is null or not a number.
     */
    public static int toIntSafe(String str, int defaultValue) {
        try {
            return (str == null) ? defaultValue : Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Converts Integer to String.
     */
    public static String toString(Integer value) {
        return (value == null) ? "" : String.valueOf(value);
    }
}
