package com.threedcger.lib.obj.model;

import java.util.Arrays;
import java.util.List;

public class Material {
    private String name;
    private List<Double> ambientColor = Arrays.asList(0.0, 0.0, 0.0, 1.0);  // Ka
    private List<Double> emissiveColor = Arrays.asList(0.0, 0.0, 0.0, 1.0);  // Ke
    private List<Double> diffuseColor = Arrays.asList(0.5, 0.5, 0.5, 1.0);  // Kd
    private List<Double> specularColor = Arrays.asList(0.0, 0.0, 0.0, 1.0);  // Ks
    private Double specularShininess = 0.0;  // Ns
    private Double alpha = 0.0;  // d / Tr
    private Texture ambientTexture;  // map_Ka
    private String ambientTexturePath;  // map_Ka
    private Texture emissiveTexture;  // map_Ke
    private String emissiveTexturePath;  // map_Ke
    private Texture diffuseTexture;  // map_Kd
    private String diffuseTexturePath;  // map_Kd
    private Texture specularTexture;  // map_Ks
    private String specularTexturePath;  // map_Ks
    private Texture specularShininessTexture;  // map_Ns
    private String specularShininessTexturePath;  // map_Ns
    private Texture normalTexture;  // map_Bump
    private String normalTexturePath;  // map_Bump
    private Texture alphaTexture;  // map_d
    private String alphaTexturePath;  // map_d

    public String getTexturePath(String name) {
        if ("ambientTexture".equals(name)) return ambientTexturePath;
        if ("emissiveTexture".equals(name)) return emissiveTexturePath;
        if ("diffuseTexture".equals(name)) return diffuseTexturePath;
        if ("specularTexture".equals(name)) return specularTexturePath;
        if ("specularShininessTexture".equals(name)) return specularShininessTexturePath;
        if ("normalTexture".equals(name)) return normalTexturePath;
        if ("alphaTexture".equals(name)) return alphaTexturePath;
        return null;
    }
    public void setTexture(String name, Texture texture) {
        if ("ambientTexture".equals(name)) this.ambientTexture = texture;
        if ("emissiveTexture".equals(name)) this.emissiveTexture = texture;
        if ("diffuseTexture".equals(name)) this.diffuseTexture = texture;
        if ("specularTexture".equals(name)) this.specularTexture = texture;
        if ("specularShininessTexture".equals(name)) this.specularShininessTexture = texture;
        if ("normalTexture".equals(name)) this.normalTexture = texture;
        if ("alphaTexture".equals(name)) this.alphaTexture = texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(List<Double> ambientColor) {
        this.ambientColor = ambientColor;
    }

    public List<Double> getEmissiveColor() {
        return emissiveColor;
    }

    public void setEmissiveColor(List<Double> emissiveColor) {
        this.emissiveColor = emissiveColor;
    }

    public List<Double> getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(List<Double> diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public List<Double> getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(List<Double> specularColor) {
        this.specularColor = specularColor;
    }

    public Double getSpecularShininess() {
        return specularShininess;
    }

    public void setSpecularShininess(Double specularShininess) {
        this.specularShininess = specularShininess;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public Texture getAmbientTexture() {
        return ambientTexture;
    }

    public void setAmbientTexture(Texture ambientTexture) {
        this.ambientTexture = ambientTexture;
    }

    public String getAmbientTexturePath() {
        return ambientTexturePath;
    }

    public void setAmbientTexturePath(String ambientTexturePath) {
        this.ambientTexturePath = ambientTexturePath;
    }

    public Texture getEmissiveTexture() {
        return emissiveTexture;
    }

    public void setEmissiveTexture(Texture emissiveTexture) {
        this.emissiveTexture = emissiveTexture;
    }

    public String getEmissiveTexturePath() {
        return emissiveTexturePath;
    }

    public void setEmissiveTexturePath(String emissiveTexturePath) {
        this.emissiveTexturePath = emissiveTexturePath;
    }

    public Texture getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(Texture diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    public String getDiffuseTexturePath() {
        return diffuseTexturePath;
    }

    public void setDiffuseTexturePath(String diffuseTexturePath) {
        this.diffuseTexturePath = diffuseTexturePath;
    }

    public Texture getSpecularTexture() {
        return specularTexture;
    }

    public void setSpecularTexture(Texture specularTexture) {
        this.specularTexture = specularTexture;
    }

    public String getSpecularTexturePath() {
        return specularTexturePath;
    }

    public void setSpecularTexturePath(String specularTexturePath) {
        this.specularTexturePath = specularTexturePath;
    }

    public Texture getSpecularShininessTexture() {
        return specularShininessTexture;
    }

    public void setSpecularShininessTexture(Texture specularShininessTexture) {
        this.specularShininessTexture = specularShininessTexture;
    }

    public String getSpecularShininessTexturePath() {
        return specularShininessTexturePath;
    }

    public void setSpecularShininessTexturePath(String specularShininessTexturePath) {
        this.specularShininessTexturePath = specularShininessTexturePath;
    }

    public Texture getNormalTexture() {
        return normalTexture;
    }

    public void setNormalTexture(Texture normalTexture) {
        this.normalTexture = normalTexture;
    }

    public String getNormalTexturePath() {
        return normalTexturePath;
    }

    public void setNormalTexturePath(String normalTexturePath) {
        this.normalTexturePath = normalTexturePath;
    }

    public Texture getAlphaTexture() {
        return alphaTexture;
    }

    public void setAlphaTexture(Texture alphaTexture) {
        this.alphaTexture = alphaTexture;
    }

    public String getAlphaTexturePath() {
        return alphaTexturePath;
    }

    public void setAlphaTexturePath(String alphaTexturePath) {
        this.alphaTexturePath = alphaTexturePath;
    }
}
