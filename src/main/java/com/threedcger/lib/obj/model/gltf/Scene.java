package com.threedcger.lib.obj.model.gltf;

import lombok.Data;

import java.util.List;

@Data
public class Scene {
    private String name;
    private List<Integer> nodes;
    private Object extensions;
    private Object extras;
}
