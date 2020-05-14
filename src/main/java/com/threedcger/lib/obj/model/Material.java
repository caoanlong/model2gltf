package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
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
}
