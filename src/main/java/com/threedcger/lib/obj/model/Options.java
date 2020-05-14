package com.threedcger.lib.obj.model;

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

    public Boolean getBinary() {
        return binary;
    }

    public void setBinary(Boolean binary) {
        this.binary = binary;
    }

    public Boolean getSeparate() {
        return separate;
    }

    public void setSeparate(Boolean separate) {
        this.separate = separate;
    }

    public Boolean getSeparateTextures() {
        return separateTextures;
    }

    public void setSeparateTextures(Boolean separateTextures) {
        this.separateTextures = separateTextures;
    }

    public Boolean getCheckTransparency() {
        return checkTransparency;
    }

    public void setCheckTransparency(Boolean checkTransparency) {
        this.checkTransparency = checkTransparency;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public Boolean getPackOcclusion() {
        return packOcclusion;
    }

    public void setPackOcclusion(Boolean packOcclusion) {
        this.packOcclusion = packOcclusion;
    }

    public Boolean getMetallicRoughness() {
        return metallicRoughness;
    }

    public void setMetallicRoughness(Boolean metallicRoughness) {
        this.metallicRoughness = metallicRoughness;
    }

    public Boolean getSpecularGlossiness() {
        return specularGlossiness;
    }

    public void setSpecularGlossiness(Boolean specularGlossiness) {
        this.specularGlossiness = specularGlossiness;
    }

    public Boolean getUnlit() {
        return unlit;
    }

    public void setUnlit(Boolean unlit) {
        this.unlit = unlit;
    }

    public String getInputUpAxis() {
        return inputUpAxis;
    }

    public void setInputUpAxis(String inputUpAxis) {
        this.inputUpAxis = inputUpAxis;
    }

    public String getOutputUpAxis() {
        return outputUpAxis;
    }

    public void setOutputUpAxis(String outputUpAxis) {
        this.outputUpAxis = outputUpAxis;
    }

    public OverridingTextures getOverridingTextures() {
        return overridingTextures;
    }

    public void setOverridingTextures(OverridingTextures overridingTextures) {
        this.overridingTextures = overridingTextures;
    }
}
