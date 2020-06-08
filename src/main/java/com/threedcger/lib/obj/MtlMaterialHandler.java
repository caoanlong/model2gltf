package com.threedcger.lib.obj;

import com.threedcger.lib.gltf.model.*;

public class MtlMaterialHandler {
    private final TextureHandler textureHandler;

    private Float metalness;
    private String metalnessMap;
    private Float roughness;
    private String roughnessMap;
    private String color;
    private String map;
    private String emissive;
    private Float emissiveIntensity;
    private String emissiveMap;
    private Float aoMapIntensity;
    private String aoMap;
    private String normalMap;
    private Float opacity;

    public MtlMaterialHandler(GlTF glTF, MtlDto mtlDto) {
        this.textureHandler = new TextureHandler(glTF);

        this.metalness = mtlDto.getMetalness();
        this.metalnessMap = mtlDto.getMetalnessMap();
        this.roughness = mtlDto.getRoughness();
        this.roughnessMap = mtlDto.getRoughnessMap();
        this.color = mtlDto.getColor();
        this.map = mtlDto.getMap();
        this.emissive = mtlDto.getEmissive();
        this.emissiveIntensity = mtlDto.getEmissiveIntensity();
        this.emissiveMap = mtlDto.getEmissiveMap();
        this.aoMapIntensity = mtlDto.getAoMapIntensity();
        this.aoMap = mtlDto.getAoMap();
        this.normalMap = mtlDto.getNormalMap();
        this.opacity = mtlDto.getOpacity();
    }

    public Material createMaterial() {
        Material material = new Material();
        if (    color != null
                || map != null
                || metalness != null
                || metalnessMap != null
                || roughness != null
                || roughnessMap != null
                || normalMap != null
                || emissiveMap != null
                || aoMap != null
                || opacity != null
                ) {
            PbrMetallicRoughness pbrMetallicRoughness = new PbrMetallicRoughness();
            if (map != null) {
                int textureIndex = textureHandler.getTextureIndex(map);
                TextureInfo baseColorTexture = new TextureInfo();
                baseColorTexture.setIndex(textureIndex);
                pbrMetallicRoughness.setBaseColorTexture(baseColorTexture);
            } else {
                if (color != null) {
                    float[] baseColorFactor = hexToRGBA(color, opacity);
                    pbrMetallicRoughness.setBaseColorFactor(baseColorFactor);
                }
            }

            // 设置金属因子
            if (metalness != null) pbrMetallicRoughness.setMetallicFactor(metalness);
            // 设置粗糙因子
            if (roughness != null) pbrMetallicRoughness.setRoughnessFactor(roughness);

            if (metalnessMap != null || roughnessMap != null) {
                int textureIndex;
                if (metalnessMap != null) {
                    textureIndex = textureHandler.getTextureIndex(metalnessMap);
                } else {
                    textureIndex = textureHandler.getTextureIndex(roughnessMap);
                }
                TextureInfo metallicRoughnessTexture = new TextureInfo();
                metallicRoughnessTexture.setIndex(textureIndex);
                pbrMetallicRoughness.setMetallicRoughnessTexture(metallicRoughnessTexture);
            }

            material.setPbrMetallicRoughness(pbrMetallicRoughness);

            if (normalMap != null) {
                int textureIndex = textureHandler.getTextureIndex(normalMap);
                NormalTextureInfo normalTexture = new NormalTextureInfo();
                normalTexture.setIndex(textureIndex);
                material.setNormalTexture(normalTexture);
            }
            if (aoMap != null) {
                int textureIndex = textureHandler.getTextureIndex(aoMap);
                OcclusionTextureInfo occlusionTexture = new OcclusionTextureInfo();
                occlusionTexture.setIndex(textureIndex);
                if (aoMapIntensity != null) occlusionTexture.setStrength(aoMapIntensity);
                material.setOcclusionTexture(occlusionTexture);
            }
            if (emissiveMap != null) {
                int textureIndex = textureHandler.getTextureIndex(emissiveMap);
                TextureInfo emissiveTexture = new TextureInfo();
                emissiveTexture.setIndex(textureIndex);
                material.setEmissiveTexture(emissiveTexture);
            }
            if (emissive != null) {
                System.out.println(emissive);
                float[] emissiveFactor = hexToRGB(emissive);
                material.setEmissiveFactor(emissiveFactor);
            }

            if (opacity != null && opacity < 1.0f) {
                material.setAlphaMode("BLEND");
            }
            material.setDoubleSided(true);
            return material;
        }
        return createMaterialWithColor(0.75f, 0.75f, 0.75f);
    }

    public Material createMaterialWithColor(float r, float g, float b) {
        PbrMetallicRoughness pbrMetallicRoughness = new PbrMetallicRoughness();
        float[] baseColorFactor = new float[] { r, g, b, 1.0f };
        pbrMetallicRoughness.setBaseColorFactor(baseColorFactor);
        pbrMetallicRoughness.setMetallicFactor(0.0f);

        Material material = new Material();
        material.setPbrMetallicRoughness(pbrMetallicRoughness);

        return material;
    }

    /**
     * 颜色 16进制转RGB
     * @return
     */
    private float[] hexToRGBA(String color, Float alpha) {
        float r = Integer.valueOf(color.substring(1, 3), 16).floatValue() / 255.0f;
        float g = Integer.valueOf(color.substring(3, 5), 16).floatValue() / 255.0f;
        float b = Integer.valueOf(color.substring(5, 7), 16).floatValue() / 255.0f;
        if (alpha != null) return new float[] { r, g, b, alpha};
        return new float[] { r, g, b, 1.0f};
    }
    private float[] hexToRGB(String color) {
        float r = Integer.valueOf(color.substring(1, 3), 16).floatValue() / 255.0f;
        float g = Integer.valueOf(color.substring(3, 5), 16).floatValue() / 255.0f;
        float b = Integer.valueOf(color.substring(5, 7), 16).floatValue() / 255.0f;
        return new float[] { r, g, b};
    }
}
