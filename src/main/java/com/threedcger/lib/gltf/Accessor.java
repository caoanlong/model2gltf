package com.threedcger.lib.gltf;

import lombok.Data;

@Data
public class Accessor {
    private String name;
    private Integer bufferView;
    private Integer byteOffset;
    private Integer componentType;
    private Integer count;
    private String type;
    private Number[] min;
    private Number[] max;
    private Boolean normalized;
}
