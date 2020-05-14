package com.threedcger.lib.obj.model;

public class TextureOptions {
    private Boolean checkTransparency = false;
    private Boolean decode = false;
    private Boolean keepSource = false;

    public Boolean getCheckTransparency() {
        return checkTransparency;
    }

    public void setCheckTransparency(Boolean checkTransparency) {
        this.checkTransparency = checkTransparency;
    }

    public Boolean getDecode() {
        return decode;
    }

    public void setDecode(Boolean decode) {
        this.decode = decode;
    }

    public Boolean getKeepSource() {
        return keepSource;
    }

    public void setKeepSource(Boolean keepSource) {
        this.keepSource = keepSource;
    }
}
