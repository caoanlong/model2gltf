package com.threedcger.lib.gltf;

import com.threedcger.lib.gltf.model.Material;
import com.threedcger.lib.gltf.model.PbrMetallicRoughness;

public class Materials {
    /**
     * Create a {@link Material} with all default values
     *
     * @return The {@link Material}
     */
    public static Material createDefaultMaterial() {
        Material material = new Material();
        material.setPbrMetallicRoughness(createDefaultMaterialPbrMetallicRoughness());
        material.setNormalTexture(null);
        material.setOcclusionTexture(null);
        material.setEmissiveTexture(null);
        material.setEmissiveFactor(material.defaultEmissiveFactor());
        material.setAlphaMode(material.defaultAlphaMode());
        material.setAlphaCutoff(material.defaultAlphaCutoff());
        material.setDoubleSided(material.defaultDoubleSided());
        return material;
    }

    /**
     * Create a {@link PbrMetallicRoughness} with all default values
     *
     * @return The {@link PbrMetallicRoughness}
     */
    public static PbrMetallicRoughness createDefaultMaterialPbrMetallicRoughness() {
        PbrMetallicRoughness result = new PbrMetallicRoughness();
        result.setBaseColorFactor(result.defaultBaseColorFactor());
        result.setMetallicFactor(result.defaultMetallicFactor());
        result.setRoughnessFactor(result.defaultRoughnessFactor());
        return result;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Materials()
    {
        // Private constructor to prevent instantiation
    }
}
