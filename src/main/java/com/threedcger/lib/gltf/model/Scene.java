package com.threedcger.lib.gltf.model;

import lombok.Data;

import java.util.List;

@Data
public class Scene {
    private String name;
    private List<Integer> nodes;
}
