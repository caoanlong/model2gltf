package com.threedcger.lib.gltf;

import lombok.Data;

@Data
public class Sampler {
    private String name;
    private Integer magFilter;
    private Integer minFilter;
    private Integer wrapS;
    private Integer wrapT;
}
