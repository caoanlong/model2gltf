package com.threedcger.lib.obj.model;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private String name;
    private List<Primitive> primitives = new ArrayList<Primitive>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Primitive> getPrimitives() {
        return primitives;
    }

    public void setPrimitives(List<Primitive> primitives) {
        this.primitives = primitives;
    }
}
