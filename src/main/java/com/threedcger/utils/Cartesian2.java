package com.threedcger.utils;

import java.util.List;

public class Cartesian2 {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Cartesian2() {
        this.x = 0.0;
        this.y = 0.0;
    }
    public Cartesian2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Cartesian2 fromElements(double x, double y, Cartesian2 result) {
        if (result == null) return new Cartesian2(x, y);
        result.setX(x);
        result.setY(y);
        return result;
    }

    public static double[] pack(Cartesian2 value, double[] array, Integer startingIndex) {
        if (startingIndex == null) startingIndex = 0;
        array[startingIndex++] = value.getX();
        array[startingIndex] = value.getY();
        return array;
    }

    public static double[] packArray(List<Cartesian2> array, double[] result) {
        Integer length = array.size();
        Integer resultLength = length * 2;
        if (result == null) {
            result = new double[resultLength];
        } else if (result.length != resultLength) {
            // TODO
        }
        for (int i = 0; i < length; i++) {
            Cartesian2.pack(array.get(i), result, i * 2);
        }
        return result;
    }
}
