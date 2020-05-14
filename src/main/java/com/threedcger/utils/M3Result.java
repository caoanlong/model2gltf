package com.threedcger.utils;

public class M3Result {
    private Matrix3 unitary = new Matrix3();
    private Matrix3 diagonal = new Matrix3();

    public Matrix3 getUnitary() {
        return unitary;
    }

    public void setUnitary(Matrix3 unitary) {
        this.unitary = unitary;
    }

    public Matrix3 getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(Matrix3 diagonal) {
        this.diagonal = diagonal;
    }
}
