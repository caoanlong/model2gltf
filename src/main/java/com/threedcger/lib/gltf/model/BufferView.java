package com.threedcger.lib.gltf.model;

import lombok.Data;

import java.util.Map;

@Data
public class BufferView {
    private String name;
    private Integer buffer;
    private Integer byteOffset;
    private Integer byteLength;
    private Integer byteStride;
    private Integer target;
    private Map<String, Object> extensions;
    private Object extras;
}
