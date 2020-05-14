package com.threedcger.lib.obj.model;

import lombok.Data;

@Data
public class Options {
    private Boolean binary = false;
    private Boolean separate = false;
    private Boolean separateTextures = false;
    private Boolean checkTransparency = false;
    private Boolean secure = false;
    private Boolean packOcclusion = false;
    private Boolean metallicRoughness = false;
    private Boolean specularGlossiness = false;

    /**
     * Gets or sets whether the glTF will be saved with the KHR_materials_unlit extension
     */
    private Boolean unlit = false;
    /**
     * Gets or sets the up axis of the obj
     */
    private String inputUpAxis = "Y";
    /**
     * Gets or sets the up axis of the converted glTF
     */
    private String outputUpAxis = "Y";

    private OverridingTextures overridingTextures;

    public Options() {
        this.overridingTextures = new OverridingTextures();
    }
}
