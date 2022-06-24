package com.pablohorst.booking.api.util;

public class LoggingUtil {
    public static String removeLineBreaks(String s) {
        if (s.isEmpty()) {
            return s;
        }
        return s.replaceAll("[\\t\\n]*", "");
    }
}
