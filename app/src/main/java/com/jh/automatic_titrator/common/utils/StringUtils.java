package com.jh.automatic_titrator.common.utils;

/**
 * Created by apple on 2016/10/17.
 */
public class StringUtils {
    public static boolean isNotEmpty(String s) {
        if (s != null && !s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean notEmptyAndEquals(String a, String b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.equals(b);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(byte[] src, int length) {
        if (length < src.length) {
            length = src.length;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(byte[] src, int startIndex, int endIndex) {
        if (src.length < endIndex || startIndex < 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        stringBuilder.append("0x");
        for (int i = startIndex; i <= endIndex; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String[] appendInfos(String[] a, String[] b) {
        String[] res = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            if(b.length > i) {
                res[i] = a[i] + "(" + b[i] + ")";
            } else {
                res[i] = a[i];
            }
        }
        return res;
    }

    public static String dBValueInputFormat(String value) {
        return String.format("'%s',", value);
    }

    public static String dBValueInputFormat(double value) {
        return String.format("'%s',", value);
    }
}
