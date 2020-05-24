package com.threedcger.lib.gltf;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Primitive {
    private Integer indices;
    private Integer material;
    private Integer mode;
    private Map<String, Integer> attributes;
    private List<Map<String, Integer>> targets;
    private Map<String, Object> extensions;
    private Object extras;
}
