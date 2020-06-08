package com.threedcger.lib.gltf;

import com.threedcger.lib.gltf.model.*;
import com.threedcger.utils.Optionals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for creating the {@link MaterialModel} instances that are required
 * in a {@link GltfModel}. <br>
 * <br>
 * are required for rendering.
 */
class MaterialModelHandler {
    /**
     * The logger used in this class
     */
    private static final Logger logger = Logger.getLogger(MaterialModelHandler.class.getName());
    
    /**
     * Create a {@link MaterialModel} instance for the given {@link Material}
     * 
     * @param material The {@link Material}
     * @param numJoints The number of joints
     * @return The {@link MaterialModel}
     */
    MaterialModel createMaterialModel(Material material, int numJoints) {
        MaterialModel materialModel = new MaterialModel();
        MaterialStructure materialStructure = new MaterialStructure(material, numJoints);
        /**
         *  2.0中删除 techniques 和 shader
        TechniqueModel techniqueModel = obtainTechniqueModel(materialStructure);
        materialModel.setTechniqueModel(techniqueModel);
         */
        
        PbrMetallicRoughness pbrMetallicRoughness = material.getPbrMetallicRoughness();
        if (pbrMetallicRoughness == null) {
            pbrMetallicRoughness = Materials.createDefaultMaterialPbrMetallicRoughness();
        }
        
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        
        if (Boolean.TRUE.equals(material.isDoubleSided())) {
            values.put("isDoubleSided", 1);
        } else {
            values.put("isDoubleSided", 0);
        }
        
        TextureInfo baseColorTextureInfo = pbrMetallicRoughness.getBaseColorTexture();
        if (baseColorTextureInfo != null) {
            values.put("hasBaseColorTexture", 1);
            values.put("baseColorTexCoord", materialStructure.getBaseColorTexCoordSemantic());
            values.put("baseColorTexture", baseColorTextureInfo.getIndex());
        } else {
            values.put("hasBaseColorTexture", 0);
        }
        float[] baseColorFactor = Optionals.of(
            pbrMetallicRoughness.getBaseColorFactor(),
            pbrMetallicRoughness.defaultBaseColorFactor());
        values.put("baseColorFactor", baseColorFactor);

        TextureInfo metallicRoughnessTextureInfo = 
            pbrMetallicRoughness.getMetallicRoughnessTexture();
        if (metallicRoughnessTextureInfo != null) {
            values.put("hasMetallicRoughnessTexture", 1);
            values.put("metallicRoughnessTexCoord",
                materialStructure.getMetallicRoughnessTexCoordSemantic());
            values.put("metallicRoughnessTexture", 
                metallicRoughnessTextureInfo.getIndex());
        } else {
            values.put("hasMetallicRoughnessTexture", 0);
        }
        float metallicFactor = Optionals.of(
            pbrMetallicRoughness.getMetallicFactor(),
            pbrMetallicRoughness.defaultMetallicFactor());
        values.put("metallicFactor", metallicFactor);
        
        float roughnessFactor = Optionals.of(
            pbrMetallicRoughness.getRoughnessFactor(),
            pbrMetallicRoughness.defaultRoughnessFactor());
        values.put("roughnessFactor", roughnessFactor);
        
        
        NormalTextureInfo normalTextureInfo =
            material.getNormalTexture();
        if (normalTextureInfo != null) {
            values.put("hasNormalTexture", 1);
            values.put("normalTexCoord", materialStructure.getNormalTexCoordSemantic());
            values.put("normalTexture", normalTextureInfo.getIndex());
            
            float normalScale = Optionals.of(
                normalTextureInfo.getScale(),
                normalTextureInfo.defaultScale());
            values.put("normalScale", normalScale);
        } else {
            values.put("hasNormalTexture", 0);
            values.put("normalScale", 1.0);
        }

        OcclusionTextureInfo occlusionTextureInfo = material.getOcclusionTexture();
        if (occlusionTextureInfo != null) {
            values.put("hasOcclusionTexture", 1);
            values.put("occlusionTexCoord", 
                materialStructure.getOcclusionTexCoordSemantic());
            values.put("occlusionTexture", 
                occlusionTextureInfo.getIndex());
            
            float occlusionStrength = Optionals.of(
                occlusionTextureInfo.getStrength(),
                occlusionTextureInfo.defaultStrength());
            values.put("occlusionStrength", occlusionStrength);
        } else {
            values.put("hasOcclusionTexture", 0);
            
            // TODO Should this really be 1.0?
            values.put("occlusionStrength", 0.0); 
        }

        TextureInfo emissiveTextureInfo = material.getEmissiveTexture();
        if (emissiveTextureInfo != null) {
            values.put("hasEmissiveTexture", 1);
            values.put("emissiveTexCoord",
                materialStructure.getEmissiveTexCoordSemantic());
            values.put("emissiveTexture", 
                emissiveTextureInfo.getIndex());
        } else {
            values.put("hasEmissiveTexture", 0);
        }
        
        float[] emissiveFactor = Optionals.of(
            material.getEmissiveFactor(),
            material.defaultEmissiveFactor());
        values.put("emissiveFactor", emissiveFactor);

        float lightPosition[] = { -800,500,500 };
        values.put("lightPosition", lightPosition);
        
        materialModel.setValues(values);
        
        return materialModel;
    }
}
