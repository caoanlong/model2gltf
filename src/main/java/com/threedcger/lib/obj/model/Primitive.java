package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Primitive {
    private String material;
    private List<Integer> indices = new ArrayList<Integer>();
    private List<Float> positions = new ArrayList<Float>();
    private List<Float> normals = new ArrayList<Float>();
    private List<Float> uvs = new ArrayList<Float>();
}
