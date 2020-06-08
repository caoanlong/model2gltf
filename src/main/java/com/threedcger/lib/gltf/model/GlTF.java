package com.threedcger.lib.gltf.model;

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

    public void addMaterials(Material element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Material> oldList = this.materials;
        List<Material> newList = new ArrayList<Material>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.materials = newList;
    }

    public void addSamplers(Sampler element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Sampler> oldList = this.samplers;
        List<Sampler> newList = new ArrayList<Sampler>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.samplers = newList;
    }

    public void addImages(Image element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Image> oldList = this.images;
        List<Image> newList = new ArrayList<Image>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.images = newList;
    }

    public void addTextures(Texture element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Texture> oldList = this.textures;
        List<Texture> newList = new ArrayList<Texture>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.textures = newList;
    }

    public void addMeshes(Mesh element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Mesh> oldList = this.meshes;
        List<Mesh> newList = new ArrayList<Mesh>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.meshes = newList;
    }

    public void addNodes(Node element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Node> oldList = this.nodes;
        List<Node> newList = new ArrayList<Node>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.nodes = newList;
    }

    public void addScenes(Scene element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Scene> oldList = this.scenes;
        List<Scene> newList = new ArrayList<Scene>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.scenes = newList;
    }
}
