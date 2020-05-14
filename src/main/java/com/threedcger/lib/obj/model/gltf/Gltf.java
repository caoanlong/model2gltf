package com.threedcger.lib.obj.model.gltf;

import java.util.List;

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

    public List getAccessors() {
        return accessors;
    }

    public void setAccessors(List accessors) {
        this.accessors = accessors;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public List getBuffers() {
        return buffers;
    }

    public void setBuffers(List buffers) {
        this.buffers = buffers;
    }

    public List getBufferViews() {
        return bufferViews;
    }

    public void setBufferViews(List bufferViews) {
        this.bufferViews = bufferViews;
    }

    public List getExtensionsUsed() {
        return extensionsUsed;
    }

    public void setExtensionsUsed(List extensionsUsed) {
        this.extensionsUsed = extensionsUsed;
    }

    public List getExtensionsRequired() {
        return extensionsRequired;
    }

    public void setExtensionsRequired(List extensionsRequired) {
        this.extensionsRequired = extensionsRequired;
    }

    public List getImages() {
        return images;
    }

    public void setImages(List images) {
        this.images = images;
    }

    public List getMaterials() {
        return materials;
    }

    public void setMaterials(List materials) {
        this.materials = materials;
    }

    public List getMeshes() {
        return meshes;
    }

    public void setMeshes(List meshes) {
        this.meshes = meshes;
    }

    public List getNodes() {
        return nodes;
    }

    public void setNodes(List nodes) {
        this.nodes = nodes;
    }

    public List getSamplers() {
        return samplers;
    }

    public void setSamplers(List samplers) {
        this.samplers = samplers;
    }

    public Integer getScene() {
        return scene;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

    public List getTextures() {
        return textures;
    }

    public void setTextures(List textures) {
        this.textures = textures;
    }
}
