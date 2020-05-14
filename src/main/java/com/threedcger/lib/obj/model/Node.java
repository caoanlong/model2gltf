package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    private String name;
    private List<Mesh> meshes = new ArrayList<Mesh>();
}
