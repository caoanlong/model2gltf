package com.threedcger.lib.obj.model;

import lombok.Data;

@Data
public class OverridingTextures {
    private String metallicRoughnessOcclusionTexture;
    private String specularGlossinessTexture;
    private String occlusionTexture;
    private String normalTexture;
    private String baseColorTexture;
    private String emissiveTexture;
    private String alphaTexture;
}
