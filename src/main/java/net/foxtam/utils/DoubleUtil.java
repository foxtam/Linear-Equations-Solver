package net.foxtam.utils;

public class DoubleUtil {
    public static boolean isCloseToZero(double value) {
        return areClose(value, 0.0);
    }

    public static boolean areClose(double a, double b) {
        return Math.abs(a - b) < 1.0e-9;
    }
}
