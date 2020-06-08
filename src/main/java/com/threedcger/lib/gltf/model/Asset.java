package com.threedcger.lib.gltf.model;

import lombok.Data;

import java.util.Map;

@Data
public class Asset {
    private String copyright;
    private String generator;
    private String version;
    private String minVersion;
    private Map<String, Object> extensions;
    private Object extras;
}
