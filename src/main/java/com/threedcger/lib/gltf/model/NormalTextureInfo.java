package com.threedcger.lib.gltf.model;

import lombok.Data;

@Data
public class NormalTextureInfo extends TextureInfo {
    private Float scale;

    public void setScale(Float scale) {
        if (scale == null) {
            this.scale = scale;
            return ;
        }
        this.scale = scale;
    }

    /**
     * The scalar multiplier applied to each normal vector of the normal
     * texture. (optional)<br>
     * Default: 1.0
     *
     * @return The scale
     *
     */
    public Float getScale() {
        return this.scale;
    }

    /**
     * Returns the default value of the scale<br>
     * @see #getScale
     *
     * @return The default scale
     *
     */
    public Float defaultScale() {
        return  1.0F;
    }
}
