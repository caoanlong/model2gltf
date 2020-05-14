package com.threedcger.lib.obj.model;

import lombok.Data;

import java.util.List;

@Data
public class PbrMetallicRoughness {
    private Texture baseColorTexture;
    private Texture metallicRoughnessTexture;
    private List<Double> baseColorFactor;
    private Double metallicFactor;
    private Double roughnessFactor;
}
