package com.arfat.user.utils;

public final class StringUtils {

    private StringUtils(){}

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isBlank();
    }
}
