package com.threedcger.lib.obj;

import com.threedcger.lib.obj.model.*;
import com.threedcger.lib.obj.model.gltf.Asset;
import com.threedcger.lib.obj.model.gltf.BufferState;
import com.threedcger.lib.obj.model.gltf.Gltf;
import com.threedcger.lib.obj.model.gltf.Scene;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GltfCreator {
    private Gltf gltf;
    private BufferState bufferState;
    private List<Node> nodes;
    private List<Mtl> mtls;
    private String name;

    public GltfCreator(ObjData objData, Options options) {
        this.nodes = objData.getNodes();
        this.mtls = objData.getMtls();
        this.name = objData.getName();

        // Split materials used by primitives with different types of attributes
        this.mtls = splitIncompatibleMaterials(nodes, mtls, options);

        this.gltf = new Gltf();

        Asset asset = new Asset("model2glTF", "2.0");
        ArrayList<Scene> scenes = new ArrayList<>();
        Scene scene = new Scene();
        scene.setNodes(new ArrayList<Integer>());
        scenes.add(scene);

        this.gltf.setAsset(asset);
        this.gltf.setScenes(scenes);

        this.bufferState = new BufferState();

    }
    private List<Mtl> splitIncompatibleMaterials(List<Node> nodes, List<Mtl> materials, Options options) {
        List<Mtl> splitMaterials = new ArrayList<>();
        HashMap<String, PrimitiveInfo> primitiveInfoByMaterial = new HashMap<>();
        int nodesLength = nodes.size();
        for (int i = 0; i < nodesLength; i++) {
            List<Mesh> meshes = nodes.get(i).getMeshes();
            int meshesLength = meshes.size();
            for (int j = 0; j < meshesLength; j++) {
                List<Primitive> primitives = meshes.get(j).getPrimitives();
                int primitivesLength = primitives.size();
                for (int k = 0; k < primitivesLength; k++) {
                    Primitive primitive = primitives.get(k);
                    Boolean hasUvs = primitive.getUvs().size() > 0;
                    Boolean hasNormals = primitive.getNormals().size() > 0;
                    PrimitiveInfo primitiveInfo = new PrimitiveInfo(hasUvs, hasNormals);

                    String originalMaterialName = primitive.getMaterial() != null ? primitive.getMaterial() : "default";
                    String splitMaterialName = getSplitMaterialName(originalMaterialName, primitiveInfo, primitiveInfoByMaterial);
                    primitive.setMaterial(splitMaterialName);
                    primitiveInfoByMaterial.put(splitMaterialName, primitiveInfo);

                    Mtl splitMaterial = getMaterialByName(splitMaterials, splitMaterialName);
                    if (splitMaterial != null) continue;

                    Mtl originalMaterial = getMaterialByName(materials, originalMaterialName);
                    if (originalMaterial != null) {
                        splitMaterial = cloneMaterial(originalMaterial, !hasUvs);
                    } else {
                        splitMaterial = getDefaultMaterial(options);
                    }
                    splitMaterial.setName(splitMaterialName);
                    splitMaterials.add(splitMaterial);
                }
            }
        }
        return splitMaterials;
    }
    private String getSplitMaterialName(String originalMaterialName, PrimitiveInfo primitiveInfo, HashMap<String, PrimitiveInfo> primitiveInfoByMaterial) {
        String splitMaterialName = originalMaterialName;
        int suffix = 2;
        while (primitiveInfoByMaterial.get(splitMaterialName) != null) {
            if (primitiveInfoMatch(primitiveInfo, primitiveInfoByMaterial.get(splitMaterialName))) break;
            splitMaterialName = originalMaterialName + "-" + suffix++;
        }
        return splitMaterialName;
    }
    private Mtl getMaterialByName(List<Mtl> materials, String materialName) {
        int materialsLength = materials.size();
        for (int i = 0; i < materialsLength; i++) {
            if (materialName.equals(materials.get(i).getName())) {
                return materials.get(i);
            }
        }
        return null;
    }
    private Mtl getDefaultMaterial(Options options) {
        return null;
    }
    private Mtl cloneMaterial(Mtl material, Boolean removeTextures) {
        Field[] fields = material.getClass().getDeclaredFields();


        // TODO
        // need to deepclone
        Mtl mtl = new Mtl();
        mtl.setName(material.getName());
        mtl.setExtensions(material.getExtensions());
        mtl.setEmissiveTexture(material.getEmissiveTexture());
        mtl.setNormalTexture(material.getNormalTexture());
        mtl.setOcclusionTexture(material.getOcclusionTexture());
        mtl.setEmissiveFactor(material.getEmissiveFactor());
        mtl.setAlphaMode(material.getAlphaMode());
        mtl.setDoubleSided(material.getDoubleSided());
        return mtl;
    }
    private Boolean primitiveInfoMatch(PrimitiveInfo a, PrimitiveInfo b) {
        return a.hasUvs == b.hasUvs && a.hasNormals == b.hasNormals;
    }

    private class PrimitiveInfo {
        public Boolean hasUvs;
        public Boolean hasNormals;
        public PrimitiveInfo(Boolean hasUvs, Boolean hasNormals) {
            this.hasUvs = hasUvs;
            this.hasNormals = hasNormals;
        }
    }
}
