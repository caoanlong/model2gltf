package com.threedcger.lib.gltf.model;

import lombok.Data;

@Data
public class OcclusionTextureInfo extends TextureInfo {
    private Float strength;

    public void setStrength(Float strength) {
        if (strength == null) {
            this.strength = strength;
            return ;
        }
        if (strength > 1.0D) {
            throw new IllegalArgumentException("strength > 1.0");
        }
        if (strength< 0.0D) {
            throw new IllegalArgumentException("strength < 0.0");
        }
        this.strength = strength;
    }

    /**
     * A scalar multiplier controlling the amount of occlusion applied.
     * (optional)<br>
     * Default: 1.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     *
     * @return The strength
     *
     */
    public Float getStrength() {
        return this.strength;
    }

    /**
     * Returns the default value of the strength<br>
     * @see #getStrength
     *
     * @return The default strength
     *
     */
    public Float defaultStrength() {
        return  1.0F;
    }
}
