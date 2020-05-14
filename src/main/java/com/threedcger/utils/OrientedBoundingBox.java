package com.threedcger.utils;

import java.util.List;

public class OrientedBoundingBox {
    public Cartesian3 center;
    public Matrix3 halfAxes;
    private static Cartesian3 scratchCartesian1 = new Cartesian3();
    private static Cartesian3 scratchCartesian2 = new Cartesian3();
    private static Cartesian3 scratchCartesian3 = new Cartesian3();
    private static Cartesian3 scratchCartesian4 = new Cartesian3();
    private static Cartesian3 scratchCartesian5 = new Cartesian3();
    private static Cartesian3 scratchCartesian6 = new Cartesian3();
    private static Matrix3 scratchCovarianceResult = new Matrix3();
    private static M3Result scratchEigenResult = new M3Result();

    public OrientedBoundingBox(Cartesian3 center, Matrix3 halfAxes) {
        this.center = Cartesian3.clone(center, null);
        this.halfAxes = Matrix3.clone(halfAxes, null);
    }
    public OrientedBoundingBox() {
        this.center = Cartesian3.clone(Cartesian3.ZERO, null);
        this.halfAxes = Matrix3.clone(Matrix3.ZERO, null);
    }

    public static OrientedBoundingBox fromPoints(List<Cartesian3> positions, OrientedBoundingBox result) {
        if (result == null) {
            result = new OrientedBoundingBox();
        }
        if (positions == null || positions.size() == 0) {
            result.halfAxes = Matrix3.clone(Matrix3.ZERO, null);
            result.center = Cartesian3.clone(Cartesian3.ZERO, null);
            return result;
        }
        Integer i = 0;
        Integer length = positions.size();
        Cartesian3 meanPoint = Cartesian3.clone(positions.get(0), scratchCartesian1);
        for (i = 1; i < length; i++) {
            Cartesian3.add(meanPoint, positions.get(i), meanPoint);
        }
        Float invLength = 1.0f / length;
        Cartesian3.multiplyByScalar(meanPoint, invLength, meanPoint);
        Float exx = 0.0f;
        Float exy = 0.0f;
        Float exz = 0.0f;
        Float eyy = 0.0f;
        Float eyz = 0.0f;
        Float ezz = 0.0f;
        Cartesian3 p = null;
        for (i = 0; i < length; i++) {
            p = Cartesian3.subtract(positions.get(i), meanPoint, scratchCartesian2);
            exx += p.getX() * p.getX();
            exy += p.getX() * p.getY();
            exz += p.getX() * p.getZ();
            eyy += p.getY() * p.getY();
            eyz += p.getY() * p.getZ();
            ezz += p.getZ() * p.getZ();
        }
        exx *= invLength;
        exy *= invLength;
        exz *= invLength;
        eyy *= invLength;
        eyz *= invLength;
        ezz *= invLength;
        Matrix3 covarianceMatrix = Matrix3.clone(scratchCovarianceResult, null);
        covarianceMatrix.set(0, exx);
        covarianceMatrix.set(1, exy);
        covarianceMatrix.set(2, exz);
        covarianceMatrix.set(3, exy);
        covarianceMatrix.set(4, eyy);
        covarianceMatrix.set(5, eyz);
        covarianceMatrix.set(6, exz);
        covarianceMatrix.set(7, eyz);
        covarianceMatrix.set(8, ezz);
        M3Result eigenDecomposition = Matrix3.computeEigenDecomposition(covarianceMatrix, scratchEigenResult);
        Matrix3 rotation = Matrix3.clone(eigenDecomposition.getUnitary(), result.halfAxes);
        Cartesian3 v1 = Matrix3.getColumn(rotation, 0, scratchCartesian4);
        Cartesian3 v2 = Matrix3.getColumn(rotation, 1, scratchCartesian5);
        Cartesian3 v3 = Matrix3.getColumn(rotation, 2, scratchCartesian6);

        Float u1 = -Float.MAX_VALUE;
        Float u2 = -Float.MAX_VALUE;
        Float u3 = -Float.MAX_VALUE;
        Float l1 = Float.MAX_VALUE;
        Float l2 = Float.MAX_VALUE;
        Float l3 = Float.MAX_VALUE;

        for (i = 0; i < length; i++) {
            p = positions.get(i);
            u1 = Math.max(Cartesian3.dot(v1, p), u1);
            u2 = Math.max(Cartesian3.dot(v2, p), u2);
            u3 = Math.max(Cartesian3.dot(v3, p), u3);
            l1 = Math.min(Cartesian3.dot(v1, p), l1);
            l2 = Math.min(Cartesian3.dot(v2, p), l2);
            l3 = Math.min(Cartesian3.dot(v3, p), l3);
        }
        v1 = Cartesian3.multiplyByScalar(v1, 0.5f * (l1 + u1), v1);
        v2 = Cartesian3.multiplyByScalar(v2, 0.5f * (l2 + u2), v2);
        v3 = Cartesian3.multiplyByScalar(v3, 0.5f * (l3 + u3), v3);

        Cartesian3 center = Cartesian3.add(v1, v2, result.center);
        Cartesian3.add(center, v3, center);

        Cartesian3 scale = OrientedBoundingBox.scratchCartesian3;
        scale.setX(u1 - l1);
        scale.setY(u2 - l2);
        scale.setZ(u3 - l3);
        Cartesian3.multiplyByScalar(scale, 0.5f, scale);
        Matrix3.multiplyByScale(result.halfAxes, scale, result.halfAxes);
        return result;
    }
}
