package com.threedcger.utils;

public class Axis {
    public static Float x = 0.0f;
    public static Float y = 1.0f;
    public static Float z = 2.0f;

    public static Matrix4 Y_UP_TO_Z_UP = Matrix4.fromRotationTranslation(Matrix3.fromRotationX(CMath.PI_OVER_TWO.floatValue(), null), null, null);
    public static Matrix4 Z_UP_TO_Y_UP = Matrix4.fromRotationTranslation(Matrix3.fromRotationX(-CMath.PI_OVER_TWO.floatValue(), null), null, null);
    public static Matrix4 X_UP_TO_Z_UP = Matrix4.fromRotationTranslation(Matrix3.fromRotationY(-CMath.PI_OVER_TWO.floatValue(), null), null, null);
    public static Matrix4 Z_UP_TO_X_UP = Matrix4.fromRotationTranslation(Matrix3.fromRotationY(CMath.PI_OVER_TWO.floatValue(), null), null, null);
    public static Matrix4 X_UP_TO_Y_UP = Matrix4.fromRotationTranslation(Matrix3.fromRotationZ(CMath.PI_OVER_TWO.floatValue(), null), null, null);
    public static Matrix4 Y_UP_TO_X_UP = Matrix4.fromRotationTranslation(Matrix3.fromRotationZ(-CMath.PI_OVER_TWO.floatValue(), null), null, null);

}
