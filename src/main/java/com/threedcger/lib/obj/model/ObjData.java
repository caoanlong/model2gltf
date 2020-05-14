package com.threedcger.lib.obj.model;

import java.util.List;

public class ObjData {
    private String name;
    private List<Node> nodes;
    private List<Mtl> mtls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Mtl> getMtls() {
        return mtls;
    }

    public void setMtls(List<Mtl> mtls) {
        this.mtls = mtls;
    }
}
