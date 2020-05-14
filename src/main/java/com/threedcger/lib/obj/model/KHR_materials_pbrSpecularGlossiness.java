package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.List;

@Data
public class KHR_materials_pbrSpecularGlossiness {
    private Texture diffuseTexture;
    private Texture specularGlossinessTexture;
    private List<Double> diffuseFactor;
    private List<Double> specularFactor;
    private Double glossinessFactor;
}
