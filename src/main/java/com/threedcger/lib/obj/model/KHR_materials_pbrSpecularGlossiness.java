package com.threedcger.lib.obj.model;

import java.util.List;

public class KHR_materials_pbrSpecularGlossiness {
    private Texture diffuseTexture;
    private Texture specularGlossinessTexture;
    private List<Double> diffuseFactor;
    private List<Double> specularFactor;
    private Double glossinessFactor;

    public Texture getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(Texture diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    public Texture getSpecularGlossinessTexture() {
        return specularGlossinessTexture;
    }

    public void setSpecularGlossinessTexture(Texture specularGlossinessTexture) {
        this.specularGlossinessTexture = specularGlossinessTexture;
    }

    public List<Double> getDiffuseFactor() {
        return diffuseFactor;
    }

    public void setDiffuseFactor(List<Double> diffuseFactor) {
        this.diffuseFactor = diffuseFactor;
    }

    public List<Double> getSpecularFactor() {
        return specularFactor;
    }

    public void setSpecularFactor(List<Double> specularFactor) {
        this.specularFactor = specularFactor;
    }

    public Double getGlossinessFactor() {
        return glossinessFactor;
    }

    public void setGlossinessFactor(Double glossinessFactor) {
        this.glossinessFactor = glossinessFactor;
    }
}
