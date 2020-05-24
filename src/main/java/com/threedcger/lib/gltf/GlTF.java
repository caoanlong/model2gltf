package com.threedcger.lib.gltf;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GlTF {
    private Asset asset;
    private Integer scene;
    private List<Scene> scenes;
    private List<Node> nodes;
    private List<Mesh> meshes;
    private List<Accessor> accessors;
    private List<BufferView> bufferViews;
    private List<Buffer> buffers;
    private List<Material> materials;
    private List<Texture> textures;
    private List<Image> images;
    private List<Sampler> samplers;

    private Map<String, Object> extensions;
    private Object extras;
}
