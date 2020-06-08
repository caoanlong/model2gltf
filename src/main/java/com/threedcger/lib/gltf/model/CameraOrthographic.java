package com.threedcger.lib.gltf.model;

import lombok.Data;

import java.util.Map;

@Data
public class CameraOrthographic {
    /**
     * The floating-point horizontal magnification of the view. (required)
     *
     */
    private Float xmag;
    /**
     * The floating-point vertical magnification of the view. (required)
     *
     */
    private Float ymag;
    /**
     * The floating-point distance to the far clipping plane. `zfar` must be
     * greater than `znear`. (required)<br>
     * Minimum: 0.0 (exclusive)
     *
     */
    private Float zfar;
    /**
     * The floating-point distance to the near clipping plane. (required)<br>
     * Minimum: 0.0 (inclusive)
     *
     */
    private Float znear;

    private Map<String, Object> extensions;
    private Object extras;
}
