package com.threedcger.lib.gltf2;

import com.threedcger.lib.gltf.model.*;
import lombok.Data;

import java.util.ArrayList;
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
    private List<Animation> animations;
    private List<Camera> cameras;
    private Map<String, Object> extensions;
    private Object extras;
}
