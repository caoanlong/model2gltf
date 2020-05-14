package com.threedcger.lib.obj.model.gltf;

import lombok.Data;

@Data
public class Asset {
    private String generator;
    private String version;

    public Asset(String generator, String version) {
        this.generator = generator;
        this.version = version;
    }
}
