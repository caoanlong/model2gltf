package com.threedcger.utils;

import lombok.Data;

@Data
public class M3Result {
    private Matrix3 unitary = new Matrix3();
    private Matrix3 diagonal = new Matrix3();
}
