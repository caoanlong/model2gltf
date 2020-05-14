package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.List;

@Data
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
}
