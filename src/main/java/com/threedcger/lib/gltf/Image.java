package com.threedcger.lib.gltf;

import lombok.Data;

@Data
public class Image {
    private String name;
    private String uri;
    private String mimeType;
    private Integer bufferView;
}
