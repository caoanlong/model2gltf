package com.threedcger.lib.obj.model;

import java.util.ArrayList;
import java.util.List;

public class Primitive {
    private String material;
    private List<Integer> indices = new ArrayList<Integer>();
    private List<Float> positions = new ArrayList<Float>();
    private List<Float> normals = new ArrayList<Float>();
    private List<Float> uvs = new ArrayList<Float>();

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public List<Float> getPositions() {
        return positions;
    }

    public void setPositions(List<Float> positions) {
        this.positions = positions;
    }

    public List<Float> getNormals() {
        return normals;
    }

    public void setNormals(List<Float> normals) {
        this.normals = normals;
    }

    public List<Float> getUvs() {
        return uvs;
    }

    public void setUvs(List<Float> uvs) {
        this.uvs = uvs;
    }
}
