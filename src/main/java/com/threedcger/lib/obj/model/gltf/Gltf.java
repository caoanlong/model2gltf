package com.threedcger.lib.obj.model.gltf;

import lombok.Data;

import java.util.List;

@Data
public class Gltf {
    private List accessors;
    private Asset asset;
    private List buffers;
    private List bufferViews;
    private List extensionsUsed;
    private List extensionsRequired;
    private List images;
    private List materials;
    private List meshes;
    private List nodes;
    private List samplers;
    private Integer scene;
    private List<Scene> scenes;
    private List textures;
}
