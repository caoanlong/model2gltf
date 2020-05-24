package com.threedcger.lib.gltf;

import lombok.Data;

@Data
public class Material {
    private String name;
    private PbrMetallicRoughness pbrMetallicRoughness;
    private NormalTextureInfo normalTexture;
    private OcclusionTextureInfo occlusionTexture;
    private TextureInfo emissiveTexture;
    private float[] emissiveFactor;
    private String alphaMode;
    private Float alphaCutoff;
    private Boolean doubleSided;
}
