package com.miraeasset.cyber.cmmn.util;

public class StringUtil {

    public static String nvl(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }
}
