package com.threedcger.lib.gltf2;

import lombok.Data;

@Data
public class MtlDto {
    private String name;
    private Float metalness;
    private String metalnessMap;
    private Float roughness;
    private String roughnessMap;
    private String color;
    private String map;
    private String emissive;
    private Float emissiveIntensity;
    private String emissiveMap;
    private Float aoMapIntensity;
    private String aoMap;
    private String normalMap;
    private Float opacity;
}
