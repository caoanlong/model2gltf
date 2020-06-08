package com.threedcger.lib.gltf.model;

import lombok.Data;

import java.util.Map;

@Data
public class CameraPerspective {
    /**
     * The floating-point aspect ratio of the field of view. (optional)<br>
     * Minimum: 0.0 (exclusive)
     *
     */
    private Float aspectRatio;
    /**
     * The floating-point vertical field of view in radians. (required)<br>
     * Minimum: 0.0 (exclusive)
     *
     */
    private Float yfov;
    /**
     * The floating-point distance to the far clipping plane. (optional)<br>
     * Minimum: 0.0 (exclusive)
     *
     */
    private Float zfar;
    /**
     * The floating-point distance to the near clipping plane. (required)<br>
     * Minimum: 0.0 (exclusive)
     *
     */
    private Float znear;

    private Map<String, Object> extensions;
    private Object extras;
}
