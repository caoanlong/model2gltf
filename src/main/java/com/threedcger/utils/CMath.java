package com.threedcger.utils;

public class CMath {
    public final static Double EPSILON1 = 0.1;
    public final static Double EPSILON2 = 0.01;
    public final static Double EPSILON3 = 0.001;
    public final static Double EPSILON4 = 0.0001;
    public final static Double EPSILON5 = 0.00001;
    public final static Double EPSILON6 = 0.000001;
    public final static Double EPSILON7 = 0.0000001;
    public final static Double EPSILON8 = 0.00000001;
    public final static Double EPSILON9 = 0.000000001;
    public final static Double EPSILON10 = 0.0000000001;
    public final static Double EPSILON11 = 0.00000000001;
    public final static Double EPSILON12 = 0.000000000001;
    public final static Double EPSILON13 = 0.0000000000001;
    public final static Double EPSILON14 = 0.00000000000001;
    public final static Double EPSILON15 = 0.000000000000001;
    public final static Double EPSILON16 = 0.0000000000000001;
    public final static Double EPSILON17 = 0.00000000000000001;
    public final static Double EPSILON18 = 0.000000000000000001;
    public final static Double EPSILON19 = 0.0000000000000000001;
    public final static Double EPSILON20 = 0.00000000000000000001;
    public final static Double EPSILON21 = 0.000000000000000000001;

    public final static Double PI = Math.PI;
    public final static Double ONE_OVER_PI = 1.0 / Math.PI;
    public final static Double PI_OVER_TWO = Math.PI / 2.0;
    public final static Double PI_OVER_THREE = Math.PI / 3.0;
    public final static Double PI_OVER_FOUR = Math.PI / 4.0;
    public final static Double PI_OVER_SIX = Math.PI / 6.0;

    public static Double clamp(Double value, Double min, Double max) {
        if (value == null) {
            throw new RuntimeException("value is required");
        }
        if (min == null) {
            throw new RuntimeException("min is required");
        }
        if (max == null) {
            throw new RuntimeException("max is required");
        }
        return value < min ? min : value > max ? max : value;
    }
}
