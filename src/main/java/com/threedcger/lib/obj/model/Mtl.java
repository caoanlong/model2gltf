package com.threedcger.lib.obj.model;

import java.util.List;

public class Mtl {
    private String name;
    private Extensions extensions;
    private PbrMetallicRoughness pbrMetallicRoughness;
    private Texture emissiveTexture;
    private Texture normalTexture;
    private Texture occlusionTexture;
    private List<Double> emissiveFactor;
    private String alphaMode;
    private Boolean doubleSided;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }

    public PbrMetallicRoughness getPbrMetallicRoughness() {
        return pbrMetallicRoughness;
    }

    public void setPbrMetallicRoughness(PbrMetallicRoughness pbrMetallicRoughness) {
        this.pbrMetallicRoughness = pbrMetallicRoughness;
    }

    public Texture getEmissiveTexture() {
        return emissiveTexture;
    }

    public void setEmissiveTexture(Texture emissiveTexture) {
        this.emissiveTexture = emissiveTexture;
    }

    public Texture getNormalTexture() {
        return normalTexture;
    }

    public void setNormalTexture(Texture normalTexture) {
        this.normalTexture = normalTexture;
    }

    public Texture getOcclusionTexture() {
        return occlusionTexture;
    }

    public void setOcclusionTexture(Texture occlusionTexture) {
        this.occlusionTexture = occlusionTexture;
    }

    public List<Double> getEmissiveFactor() {
        return emissiveFactor;
    }

    public void setEmissiveFactor(List<Double> emissiveFactor) {
        this.emissiveFactor = emissiveFactor;
    }

    public String getAlphaMode() {
        return alphaMode;
    }

    public void setAlphaMode(String alphaMode) {
        this.alphaMode = alphaMode;
    }

    public Boolean getDoubleSided() {
        return doubleSided;
    }

    public void setDoubleSided(Boolean doubleSided) {
        this.doubleSided = doubleSided;
    }

}
