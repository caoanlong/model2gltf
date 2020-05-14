package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Mesh {
    private String name;
    private List<Primitive> primitives = new ArrayList<Primitive>();
}
