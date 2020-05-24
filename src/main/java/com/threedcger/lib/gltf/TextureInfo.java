package com.threedcger.lib.gltf;

import lombok.Data;

import java.util.Map;

@Data
public class TextureInfo {
    private Integer index;
    private Integer texCoord;
    private Map<String, Object> extensions;
    private Object extras;
}
