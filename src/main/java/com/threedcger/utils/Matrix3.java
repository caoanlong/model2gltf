package com.threedcger.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix3 extends ArrayList<Float> {
    public final static Integer packedLength = 9;
    public Matrix3() {
        this.add(0.0f);
        this.add(0.0f);
        this.add(0.0f);
        this.add(0.0f);
        this.add(0.0f);
        this.add(0.0f);
        this.add(0.0f);
        this.add(0.0f);
        this.add(0.0f);
    }
    public Matrix3(
            Float column0Row0,
            Float column1Row0,
            Float column2Row0,
            Float column0Row1,
            Float column1Row1,
            Float column2Row1,
            Float column0Row2,
            Float column1Row2,
            Float column2Row2
    ) {
        this.add(column0Row0 != null ? column0Row0 : 0.0f);
        this.add(column1Row0 != null ? column1Row0 : 0.0f);
        this.add(column2Row0 != null ? column2Row0 : 0.0f);
        this.add(column0Row1 != null ? column0Row1 : 0.0f);
        this.add(column1Row1 != null ? column1Row1 : 0.0f);
        this.add(column2Row1 != null ? column2Row1 : 0.0f);
        this.add(column0Row2 != null ? column0Row2 : 0.0f);
        this.add(column1Row2 != null ? column1Row2 : 0.0f);
        this.add(column2Row2 != null ? column2Row2 : 0.0f);
    }

    public final static Matrix3 ZERO = new Matrix3(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    public final static Matrix3 IDENTITY = new Matrix3(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);

    public static Matrix3 jMatrix = new Matrix3();
    public static Matrix3 jMatrixTranspose = new Matrix3();

    private static List<Integer> rowVal = Arrays.asList(1, 0, 0);
    private static List<Integer> colVal = Arrays.asList(2, 2, 1);

    public static Matrix3 fromRotationX(Float angle, Matrix3 result) {
        Float cosAngle = Double.valueOf(Math.cos(angle.doubleValue())).floatValue();
        Float sinAngle = Double.valueOf(Math.sin(angle.doubleValue())).floatValue();

        if (null == result) {
            return new Matrix3(
                    1.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    cosAngle,
                    -sinAngle,
                    0.0f,
                    sinAngle,
                    cosAngle
            );
        }
        result.set(0, 1.0f);
        result.set(1, 0.0f);
        result.set(2, 0.0f);
        result.set(3, 0.0f);
        result.set(4, cosAngle);
        result.set(5, sinAngle);
        result.set(6, 0.0f);
        result.set(7, -sinAngle);
        result.set(8, cosAngle);
        return result;
    }

    public static Matrix3 fromRotationY(Float angle, Matrix3 result) {
        Float cosAngle = Double.valueOf(Math.cos(angle.doubleValue())).floatValue();
        Float sinAngle = Double.valueOf(Math.sin(angle.doubleValue())).floatValue();
        if (null == result) {
            return new Matrix3(
                    cosAngle,
                    0.0f,
                    sinAngle,
                    0.0f,
                    1.0f,
                    0.0f,
                    -sinAngle,
                    0.0f,
                    cosAngle
            );
        }
        result.set(0, cosAngle);
        result.set(1, 0.0f);
        result.set(2, -sinAngle);
        result.set(3, 0.0f);
        result.set(4, 1.0f);
        result.set(5, 0.0f);
        result.set(6, sinAngle);
        result.set(7, 0.0f);
        result.set(8, cosAngle);
        return result;
    }

    public static Matrix3 fromRotationZ(Float angle, Matrix3 result) {
        Float cosAngle = Double.valueOf(Math.cos(angle.doubleValue())).floatValue();
        Float sinAngle = Double.valueOf(Math.sin(angle.doubleValue())).floatValue();
        if (null == result) {
            return new Matrix3(
                    cosAngle,
                    -sinAngle,
                    0.0f,
                    sinAngle,
                    cosAngle,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f
            );
        }
        result.set(0, cosAngle);
        result.set(1, sinAngle);
        result.set(2, 0.0f);
        result.set(3, -sinAngle);
        result.set(4, cosAngle);
        result.set(5, 0.0f);
        result.set(6, 0.0f);
        result.set(7, 0.0f);
        result.set(8, 1.0f);
        return result;
    }

    public static Matrix3 clone(Matrix3 matrix, Matrix3 result) {
        if (result == null) {
            return new Matrix3(
                    matrix.get(0),
                    matrix.get(3),
                    matrix.get(6),
                    matrix.get(1),
                    matrix.get(4),
                    matrix.get(7),
                    matrix.get(2),
                    matrix.get(5),
                    matrix.get(8)
            );
        }
        result.set(0, matrix.get(0));
        result.set(1, matrix.get(1));
        result.set(2, matrix.get(2));
        result.set(3, matrix.get(3));
        result.set(4, matrix.get(4));
        result.set(5, matrix.get(5));
        result.set(6, matrix.get(6));
        result.set(7, matrix.get(7));
        result.set(8, matrix.get(8));
        return result;
    }

    public static M3Result computeEigenDecomposition(Matrix3 matrix, M3Result result) {
        Float tolerance = CMath.EPSILON20.floatValue();
        Integer maxSweeps = 10, count = 0, sweep = 0;

        if (result == null) result = new M3Result();

        result.setUnitary(Matrix3.clone(Matrix3.IDENTITY, result.getUnitary()));
        result.setDiagonal(Matrix3.clone(matrix, result.getDiagonal()));
        Matrix3 unitaryMatrix = result.getUnitary();
        Matrix3 diagMatrix = result.getDiagonal();

        Float epsilon = tolerance * Matrix3.computeFrobeniusNorm(diagMatrix);
        while (sweep < maxSweeps && offDiagonalFrobeniusNorm(diagMatrix) > epsilon) {
            Matrix3.shurDecomposition(diagMatrix, jMatrix);
            Matrix3.transpose(jMatrix, jMatrixTranspose);
            Matrix3.multiply(diagMatrix, jMatrix, diagMatrix);
            Matrix3.multiply(jMatrixTranspose, diagMatrix, diagMatrix);
            Matrix3.multiply(unitaryMatrix, jMatrix, unitaryMatrix);
            if (++count > 2) {
                ++sweep;
                count = 0;
            }
        }
        return result;
    }

    public static Cartesian3 getColumn(Matrix3 matrix, Integer index, Cartesian3 result) {
        Integer startIndex = index * 3;
        Float x = matrix.get(startIndex);
        Float y = matrix.get(startIndex + 1);
        Float z = matrix.get(startIndex + 2);
        result.setX(x);
        result.setY(y);
        result.setZ(z);
        return result;
    }

    public static Matrix3 multiplyByScale(Matrix3 matrix, Cartesian3 scale, Matrix3 result) {
        result.set(0, matrix.get(0) * scale.getX());
        result.set(1, matrix.get(1) * scale.getX());
        result.set(2, matrix.get(2) * scale.getX());
        result.set(3, matrix.get(3) * scale.getY());
        result.set(4, matrix.get(4) * scale.getY());
        result.set(5, matrix.get(5) * scale.getY());
        result.set(6, matrix.get(6) * scale.getZ());
        result.set(7, matrix.get(7) * scale.getZ());
        result.set(8, matrix.get(8) * scale.getZ());
        return result;
    }

    private static Float computeFrobeniusNorm(Matrix3 matrix) {
        Float norm = 0.0f;
        for (int i = 0; i < 9; i++) {
            Float temp = matrix.get(i);
            norm += temp * temp;
        }
        return Double.valueOf(Math.sqrt(norm.doubleValue())).floatValue();
    }
    private static Float offDiagonalFrobeniusNorm(Matrix3 matrix) {
        Float norm = 0.0f;
        for (int i = 0; i < 3; i++) {
            Float temp = matrix.get(Matrix3.getElementIndex(colVal.get(i), rowVal.get(i)));
            norm += 2.0f * temp * temp;
        }
        return Double.valueOf(Math.sqrt(norm.doubleValue())).floatValue();
    }
    private static Integer getElementIndex(Integer column, Integer row) {
        return column * 3 + row;
    }
    private static Matrix3 shurDecomposition(Matrix3 matrix, Matrix3 result) {
        Float tolerance = CMath.EPSILON15.floatValue();
        Float maxDiagonal = 0.0f;
        Integer rotAxis = 1;
        for (int i = 0; i < 3; i++) {
            Float temp = Math.abs(matrix.get(Matrix3.getElementIndex(colVal.get(i), rowVal.get(i))));
            if (temp > maxDiagonal) {
                rotAxis = i;
                maxDiagonal = temp;
            }
        }
        Float c = 0.0f, s = 0.0f;
        Integer p = rowVal.get(rotAxis);
        Integer q = colVal.get(rotAxis);
        if (Math.abs(matrix.get(Matrix3.getElementIndex(q, p))) > tolerance) {
            Float qq = matrix.get(Matrix3.getElementIndex(q, q));
            Float pp = matrix.get(Matrix3.getElementIndex(p, p));
            Float qp = matrix.get(Matrix3.getElementIndex(q, p));
            Double tau = (qq.doubleValue() - pp.doubleValue()) / 2.0 / qp.doubleValue();
            Double t;
            if (tau < 0.0) {
                t = -1.0 / (-tau + Math.sqrt(1.0 + tau * tau));
            } else {
                t = 1.0 / (tau + Math.sqrt(1.0 + tau * tau));
            }
            c = 1.0f / Double.valueOf(Math.sqrt(1.0 + t * t)).floatValue();
            s = t.floatValue() * c;
        }
        result = Matrix3.clone(Matrix3.IDENTITY, result);
        result.set(Matrix3.getElementIndex(p, p), c);
        result.set(Matrix3.getElementIndex(q, q), c);
        result.set(Matrix3.getElementIndex(q, p), s);
        result.set(Matrix3.getElementIndex(p, q), -s);
        return result;
    }
    private static Matrix3 transpose(Matrix3 matrix, Matrix3 result) {
        Float column0Row0 = matrix.get(0);
        Float column0Row1 = matrix.get(3);
        Float column0Row2 = matrix.get(6);
        Float column1Row0 = matrix.get(1);
        Float column1Row1 = matrix.get(4);
        Float column1Row2 = matrix.get(7);
        Float column2Row0 = matrix.get(2);
        Float column2Row1 = matrix.get(5);
        Float column2Row2 = matrix.get(8);

        result.set(0, column0Row0);
        result.set(1, column0Row1);
        result.set(2, column0Row2);
        result.set(3, column1Row0);
        result.set(4, column1Row1);
        result.set(5, column1Row2);
        result.set(6, column2Row0);
        result.set(7, column2Row1);
        result.set(8, column2Row2);
        return result;
    }
    private static Matrix3 multiply(Matrix3 left, Matrix3 right, Matrix3 result) {
        Float column0Row0 = left.get(0) * right.get(0) + left.get(3) * right.get(1) + left.get(6) * right.get(2);
        Float column0Row1 = left.get(1) * right.get(0) + left.get(4) * right.get(1) + left.get(7) * right.get(2);
        Float column0Row2 = left.get(2) * right.get(0) + left.get(5) * right.get(1) + left.get(8) * right.get(2);
        Float column1Row0 = left.get(0) * right.get(3) + left.get(3) * right.get(4) + left.get(6) * right.get(5);
        Float column1Row1 = left.get(1) * right.get(3) + left.get(4) * right.get(4) + left.get(7) * right.get(5);
        Float column1Row2 = left.get(2) * right.get(3) + left.get(5) * right.get(4) + left.get(8) * right.get(5);
        Float column2Row0 = left.get(0) * right.get(6) + left.get(3) * right.get(7) + left.get(6) * right.get(8);
        Float column2Row1 = left.get(1) * right.get(6) + left.get(4) * right.get(7) + left.get(7) * right.get(8);
        Float column2Row2 = left.get(2) * right.get(6) + left.get(5) * right.get(7) + left.get(8) * right.get(8);
        result.set(0, column0Row0);
        result.set(1, column0Row1);
        result.set(2, column0Row2);
        result.set(3, column1Row0);
        result.set(4, column1Row1);
        result.set(5, column1Row2);
        result.set(6, column2Row0);
        result.set(7, column2Row1);
        result.set(8, column2Row2);
        return result;
    }
}
