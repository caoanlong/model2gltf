package com.threedcger.lib.obj.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;
    private List<Mesh> meshes = new ArrayList<Mesh>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }
}
