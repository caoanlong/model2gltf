package com.threedcger.utils;

import java.util.ArrayList;

public class Matrix4 extends ArrayList<Float> {
    public final static Integer packedLength = 16;

    private Matrix4(
            Float column0Row0,
            Float column1Row0,
            Float column2Row0,
            Float column3Row0,
            Float column0Row1,
            Float column1Row1,
            Float column2Row1,
            Float column3Row1,
            Float column0Row2,
            Float column1Row2,
            Float column2Row2,
            Float column3Row2,
            Float column0Row3,
            Float column1Row3,
            Float column2Row3,
            Float column3Row3
    ) {
        this.add(column0Row0 != null ? column0Row0 : 0.0f);
        this.add(column0Row1 != null ? column0Row1 : 0.0f);
        this.add(column0Row2 != null ? column0Row2 : 0.0f);
        this.add(column0Row3 != null ? column0Row3 : 0.0f);
        this.add(column1Row0 != null ? column1Row0 : 0.0f);
        this.add(column1Row1 != null ? column1Row1 : 0.0f);
        this.add(column1Row2 != null ? column1Row2 : 0.0f);
        this.add(column1Row3 != null ? column1Row3 : 0.0f);
        this.add(column2Row0 != null ? column2Row0 : 0.0f);
        this.add(column2Row1 != null ? column2Row1 : 0.0f);
        this.add(column2Row2 != null ? column2Row2 : 0.0f);
        this.add(column2Row3 != null ? column2Row3 : 0.0f);
        this.add(column3Row0 != null ? column3Row0 : 0.0f);
        this.add(column3Row1 != null ? column3Row1 : 0.0f);
        this.add(column3Row2 != null ? column3Row2 : 0.0f);
        this.add(column3Row3 != null ? column3Row3 : 0.0f);
    }

    public static Matrix4 fromRotationTranslation(Matrix3 rotation, Cartesian3 translation, Matrix4 result) {
        translation = translation != null ? translation : Cartesian3.ZERO;
        if (null == result) {
            return new Matrix4(
                    rotation.get(0),
                    rotation.get(3),
                    rotation.get(6),
                    translation.getX(),
                    rotation.get(1),
                    rotation.get(4),
                    rotation.get(7),
                    translation.getY(),
                    rotation.get(2),
                    rotation.get(5),
                    rotation.get(8),
                    translation.getZ(),
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f
            );
        }
        result.set(0, rotation.get(0));
        result.set(1, rotation.get(1));
        result.set(2, rotation.get(2));
        result.set(3, 0.0f);
        result.set(4, rotation.get(3));
        result.set(5, rotation.get(4));
        result.set(6, rotation.get(5));
        result.set(7, 0.0f);
        result.set(8, rotation.get(6));
        result.set(9, rotation.get(7));
        result.set(10, rotation.get(8));
        result.set(11, 0.0f);
        result.set(12, translation.getX());
        result.set(13, translation.getY());
        result.set(14, translation.getZ());
        result.set(15, 1.0f);
        return result;
    }

    public static Cartesian3 multiplyByPoint(Matrix4 matrix, Cartesian3 cartesian, Cartesian3 result) {
        Float vX = cartesian.getX();
        Float vY = cartesian.getY();
        Float vZ = cartesian.getZ();
        Float x = matrix.get(0) * vX + matrix.get(4) * vY + matrix.get(8) * vZ + matrix.get(12);
        Float y = matrix.get(1) * vX + matrix.get(5) * vY + matrix.get(9) * vZ + matrix.get(13);
        Float z = matrix.get(2) * vX + matrix.get(6) * vY + matrix.get(10) * vZ + matrix.get(14);
        result.setX(x);
        result.setY(y);
        result.setZ(z);
        return result;
    }

    public static Cartesian3 multiplyByPointAsVector(Matrix4 matrix, Cartesian3 cartesian, Cartesian3 result) {
        Float vX = cartesian.getX();
        Float vY = cartesian.getY();
        Float vZ = cartesian.getZ();
        Float x = matrix.get(0) * vX + matrix.get(4) * vY + matrix.get(8) * vZ;
        Float y = matrix.get(1) * vX + matrix.get(5) * vY + matrix.get(9) * vZ;
        Float z = matrix.get(2) * vX + matrix.get(6) * vY + matrix.get(10) * vZ;
        result.setX(x);
        result.setY(y);
        result.setZ(z);
        return result;
    }
}
