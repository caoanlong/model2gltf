package com.threedcger.lib.gltf;

import lombok.Data;

import java.util.List;

@Data
public class Mesh {
    private String name;
    private List<Primitive> primitives;
    private float[] weights;
}
