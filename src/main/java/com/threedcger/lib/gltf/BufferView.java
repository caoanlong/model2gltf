package com.threedcger.lib.gltf;

import lombok.Data;

@Data
public class BufferView {
    private String name;
    private Integer buffer;
    private Integer byteOffset;
    private Integer byteLength;
    private Integer byteStride;
    private Integer target;
}
