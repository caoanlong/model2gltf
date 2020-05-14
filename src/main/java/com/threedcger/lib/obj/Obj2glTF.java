package com.threedcger.lib.obj;

import com.threedcger.lib.obj.model.ObjData;
import com.threedcger.lib.obj.model.Options;
import com.threedcger.utils.CTools;

import java.io.IOException;

/**
 * Author: Aaron(Cao Anlong)
 * Date: 2020-05-08
 */
public class Obj2glTF {
    private Options options;
    private String objPath;
    private ObjLoader objLoader;

    public Obj2glTF(String objPath, Options options) {
        this.objPath = objPath;
        this.options = null == options ? new Options() : options;
        this.objLoader = new ObjLoader(this.options);
    }

    public void convert() throws IOException {
        if (null == objPath) throw new RuntimeException("objPath is required");
        if (CTools.bool2Int(options.getMetallicRoughness()) + CTools.bool2Int(options.getSpecularGlossiness()) + CTools.bool2Int(options.getUnlit()) > 1) {
            throw new RuntimeException("Only one material type may be set from [metallicRoughness, specularGlossiness, unlit].");
        }
        if (null != options.getOverridingTextures().getMetallicRoughnessOcclusionTexture() && null != options.getOverridingTextures().getSpecularGlossinessTexture()) {
            throw new RuntimeException("metallicRoughnessOcclusionTexture and specularGlossinessTexture cannot both be defined.");
        }
        if (null != options.getOverridingTextures().getMetallicRoughnessOcclusionTexture()) {
            options.setMetallicRoughness(true);
            options.setSpecularGlossiness(false);
            options.setPackOcclusion(true);
        }
        if (null != options.getOverridingTextures().getSpecularGlossinessTexture()) {
            options.setMetallicRoughness(false);
            options.setSpecularGlossiness(true);
        }

        ObjData objData = objLoader.load(objPath);
        System.out.println(objData);
    }
}
