package com.lesuorac.wx.common;

public enum FilterAction {
    PASS, BLOCK;

    public static FilterAction parse(String string) {
        if ("pass".equalsIgnoreCase(string)) {
            return PASS;
        }

        if ("block".equalsIgnoreCase(string)) {
            return BLOCK;
        }

        throw new IllegalArgumentException(String.format("[%s] is not a FilterAction.", string));
    }
}
