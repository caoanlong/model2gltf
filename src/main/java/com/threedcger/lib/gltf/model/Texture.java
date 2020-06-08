package com.threedcger.lib.gltf.model;

import lombok.Data;

@Data
public class Texture {
    private String name;
    private Integer sampler;
    private Integer source;
}
