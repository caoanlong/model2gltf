package com.threedcger.lib.gltf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SceneModel {
    private String name;

    private final List<NodeModel> nodeModels;

    public SceneModel()
    {
        this.nodeModels = new ArrayList<NodeModel>();
    }

    public void addNode(NodeModel node)
    {
        nodeModels.add(node);
    }

    public List<NodeModel> getNodeModels()
    {
        return Collections.unmodifiableList(nodeModels);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
