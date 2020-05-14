package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.List;

@Data
public class ObjData {
    private String name;
    private List<Node> nodes;
    private List<Mtl> mtls;
}
