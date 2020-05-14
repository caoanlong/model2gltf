package com.threedcger.utils;

import java.util.List;

public class PolygonPipeline {
    public static List<Integer> triangulate(List<Cartesian2> positions, int[] holes) {
        double[] flattenedPositions = Cartesian2.packArray(positions, null);
        return Earcut.earcut(flattenedPositions, holes, 2);
    }

    public static Integer computeWindingOrder2D(List<Cartesian2> positions) {
        double area = computeArea2D(positions);
        return area > 0.0 ? WindingOrder.COUNTER_CLOCKWISE : WindingOrder.CLOCKWISE;
    }

    public static double computeArea2D(List<Cartesian2> positions) {
        Integer length = positions.size();
        double area = 0.0;
        for (int i0 = length - 1, i1 = 0; i1 < length; i0 = i1++) {
            Cartesian2 v0 = positions.get(i0);
            Cartesian2 v1 = positions.get(i1);
            area += v0.getX() * v1.getY() - v1.getX() * v0.getY();
        }
        return area * 0.5;
    }
}
