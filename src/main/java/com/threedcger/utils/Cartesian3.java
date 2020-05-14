package com.threedcger.utils;

public class Cartesian3 {
    private Float x;
    private Float y;
    private Float z;

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public Cartesian3() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Cartesian3(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public final static Cartesian3 ZERO = new Cartesian3(0.0f, 0.0f, 0.0f);
    public final static Cartesian3 UNIT_X = new Cartesian3(1.0f, 0.0f, 0.0f);
    public final static Cartesian3 UNIT_Y = new Cartesian3(0.0f, 1.0f, 0.0f);
    public final static Cartesian3 UNIT_Z = new Cartesian3(0.0f, 0.0f, 1.0f);

    public static Cartesian3 fromElements(Float x, Float y, Float z, Cartesian3 result) {
        if (null == result) {
            return new Cartesian3(x, y, z);
        }
        result.setX(x);
        result.setY(y);
        result.setZ(z);
        return result;
    }

    public static Boolean equals(Cartesian3 left, Cartesian3 right) {
        return left.getX() == right.getX() && left.getY() == right.getY() && left.getZ() == right.getZ();
    }

    public static Cartesian3 clone(Cartesian3 cartesian, Cartesian3 result) {
        if (null == cartesian) return null;
        if (null == result) return new Cartesian3(cartesian.getX(), cartesian.getY(), cartesian.getZ());
        result.setX(cartesian.getX());
        result.setY(cartesian.getY());
        result.setZ(cartesian.getZ());
        return result;
    }

    public static Cartesian3 normalize(Cartesian3 cartesian, Cartesian3 result) {
        Float magnitude = Cartesian3.magnitude(cartesian);
        result.setX(cartesian.getX() / magnitude);
        result.setY(cartesian.getY() / magnitude);
        result.setZ(cartesian.getZ() / magnitude);
        return result;
    }

    public static Float magnitude(Cartesian3 cartesian) {
        return Double.valueOf(Math.sqrt(Cartesian3.magnitudeSquared(cartesian).floatValue())).floatValue();
    }

    public static Float magnitudeSquared(Cartesian3 cartesian) {
        return cartesian.getX() * cartesian.getX() + cartesian.getY() * cartesian.getY() + cartesian.getZ() * cartesian.getZ();
    }

    public static Cartesian3 subtract(Cartesian3 left, Cartesian3 right, Cartesian3 result) {
        result.setX(left.getX() - right.getX());
        result.setY(left.getY() - right.getY());
        result.setZ(left.getZ() - right.getZ());
        return result;
    }

    public static Cartesian3 cross(Cartesian3 left, Cartesian3 right, Cartesian3 result) {
        Float leftX = left.getX();
        Float leftY = left.getY();
        Float leftZ = left.getZ();
        Float rightX = right.getX();
        Float rightY = right.getY();
        Float rightZ = right.getZ();
        Float x = leftY * rightZ - leftZ * rightY;
        Float y = leftZ * rightX - leftX * rightZ;
        Float z = leftX * rightY - leftY * rightX;
        result.setX(x);
        result.setY(y);
        result.setZ(z);
        return result;
    }

    public static Float dot(Cartesian3 left, Cartesian3 right) {
        return left.getX() * right.getX() + left.getY() * right.getY() + left.getZ() * right.getZ();
    }

    public static Cartesian3 add(Cartesian3 left, Cartesian3 right, Cartesian3 result) {
        result.setX(left.getX() + right.getX());
        result.setY(left.getY() + right.getY());
        result.setZ(left.getZ() + right.getZ());
        return result;
    }

    public static Cartesian3 multiplyByScalar(Cartesian3 cartesian, Float scalar, Cartesian3 result) {
        result.setX(cartesian.getX() * scalar);
        result.setY(cartesian.getY() * scalar);
        result.setZ(cartesian.getZ() * scalar);
        return result;
    }
}
