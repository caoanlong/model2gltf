package com.threedcger.lib.gltf;

import lombok.Data;

import java.util.Map;

@Data
public class PbrMetallicRoughness {
    private float[] baseColorFactor;
    private Float metallicFactor;
    private Float roughnessFactor;
    private TextureInfo baseColorTexture;
    private TextureInfo metallicRoughnessTexture;

    private Map<String, Object> extensions;
    private Object extras;
}
