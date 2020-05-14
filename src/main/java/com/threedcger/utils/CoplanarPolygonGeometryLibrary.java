package com.threedcger.utils;

import java.util.ArrayList;
import java.util.List;

public class CoplanarPolygonGeometryLibrary {
    private static Cartesian3 scratchIntersectionPoint = new Cartesian3();
    private static Cartesian3 scratchXAxis = new Cartesian3();
    private static Cartesian3 scratchYAxis = new Cartesian3();
    private static Cartesian3 scratchZAxis = new Cartesian3();
    private static OrientedBoundingBox obbScratch = new OrientedBoundingBox();

    public static Boolean computeProjectTo2DArguments(List<Cartesian3> positions, Cartesian3 centerResult, Cartesian3 planeAxis1Result, Cartesian3 planeAxis2Result) {
        OrientedBoundingBox orientedBoundingBox = OrientedBoundingBox.fromPoints(positions, obbScratch);
        Matrix3 halfAxes = orientedBoundingBox.halfAxes;
        Cartesian3 xAxis = Matrix3.getColumn(halfAxes, 0, scratchXAxis);
        Cartesian3 yAxis = Matrix3.getColumn(halfAxes, 1, scratchYAxis);
        Cartesian3 zAxis = Matrix3.getColumn(halfAxes, 2, scratchZAxis);

        Float xMag = Cartesian3.magnitude(xAxis);
        Float yMag = Cartesian3.magnitude(yAxis);
        Float zMag = Cartesian3.magnitude(xAxis);

        return !((xMag == 0.0f && (yMag == 0.0f || zMag == 0.0f)) || (yMag == 0.0f && zMag == 0.0f));
    }
    public static List<Cartesian2> createProjectPointsTo2DFunction(Cartesian3 center, Cartesian3 axis1, Cartesian3 axis2, List<Cartesian3> positions) {
        List<Cartesian2> positionResults = new ArrayList<Cartesian2>(positions.size());
        for (int i = 0; i < positionResults.size(); i++) {
            positionResults.set(i, CoplanarPolygonGeometryLibrary.projectTo2D(positions.get(i), center, axis1, axis2, null));
        }
        return positionResults;
    }
    private static Cartesian2 projectTo2D(Cartesian3 position, Cartesian3 center, Cartesian3 axis1, Cartesian3 axis2, Cartesian2 result) {
        Cartesian3 v = Cartesian3.subtract(position, center, scratchIntersectionPoint);
        double x = Cartesian3.dot(axis1, v);
        double y = Cartesian3.dot(axis2, v);
        return Cartesian2.fromElements(x, y, result);
    }
}
