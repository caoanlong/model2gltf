package com.threedcger.lib.obj.model.gltf;

public class Asset {
    private String generator;
    private String version;

    public Asset(String generator, String version) {
        this.generator = generator;
        this.version = version;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
