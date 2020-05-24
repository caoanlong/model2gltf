package com.threedcger.lib.gltf;

import lombok.Data;

@Data
public class Node {
    private String name;
    private Integer mesh;
    private Integer camera;
    private Integer skin;
    private int[] children;
    private float[] matrix;
    private float[] rotation;
    private float[] scale;
    private float[] translation;
    private float[] weights;
}
