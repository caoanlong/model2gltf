package com.threedcger.lib.gltf.model;

import lombok.Data;

import java.util.Map;

@Data
public class Image {
    private String name;
    private String uri;
    private String mimeType;
    private Integer bufferView;
    private Map<String, Object> extensions;
    private Object extras;
}
