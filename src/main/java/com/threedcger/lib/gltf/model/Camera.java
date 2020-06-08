package com.threedcger.lib.gltf.model;

import lombok.Data;

@Data
public class Camera {
    private String name;
    private CameraOrthographic orthographic;
    private CameraPerspective perspective;
    private String type;
}
