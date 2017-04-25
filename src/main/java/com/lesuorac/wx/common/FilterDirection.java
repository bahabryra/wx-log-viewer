package com.lesuorac.wx.common;

public enum FilterDirection {
    IN, OUT;

    public static FilterDirection parse(String string) {
        if ("in".equalsIgnoreCase(string)) {
            return IN;
        }

        if ("out".equalsIgnoreCase(string)) {
            return OUT;
        }

        throw new IllegalArgumentException(String.format("[%s] given string is not a FilterDirection", string));
    }

}
