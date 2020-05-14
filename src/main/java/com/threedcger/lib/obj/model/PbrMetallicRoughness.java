package com.threedcger.lib.obj.model;

import java.util.List;

public class PbrMetallicRoughness {
    private Texture baseColorTexture;
    private Texture metallicRoughnessTexture;
    private List<Double> baseColorFactor;
    private Double metallicFactor;
    private Double roughnessFactor;

    public Texture getBaseColorTexture() {
        return baseColorTexture;
    }

    public void setBaseColorTexture(Texture baseColorTexture) {
        this.baseColorTexture = baseColorTexture;
    }

    public Texture getMetallicRoughnessTexture() {
        return metallicRoughnessTexture;
    }

    public void setMetallicRoughnessTexture(Texture metallicRoughnessTexture) {
        this.metallicRoughnessTexture = metallicRoughnessTexture;
    }

    public List<Double> getBaseColorFactor() {
        return baseColorFactor;
    }

    public void setBaseColorFactor(List<Double> baseColorFactor) {
        this.baseColorFactor = baseColorFactor;
    }

    public Double getMetallicFactor() {
        return metallicFactor;
    }

    public void setMetallicFactor(Double metallicFactor) {
        this.metallicFactor = metallicFactor;
    }

    public Double getRoughnessFactor() {
        return roughnessFactor;
    }

    public void setRoughnessFactor(Double roughnessFactor) {
        this.roughnessFactor = roughnessFactor;
    }
}
