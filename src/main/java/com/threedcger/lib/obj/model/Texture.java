package com.threedcger.lib.obj.model;

import java.io.FileInputStream;
import java.nio.IntBuffer;

public class Texture {
    private Boolean transparent = false;
    private FileInputStream source;
    private String name;
    private String extension;
    private String path;
    private IntBuffer pixels;
    private Integer width;
    private Integer height;

    public Boolean getTransparent() {
        return transparent;
    }

    public void setTransparent(Boolean transparent) {
        this.transparent = transparent;
    }

    public FileInputStream getSource() {
        return source;
    }

    public void setSource(FileInputStream source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public IntBuffer getPixels() {
        return pixels;
    }

    public void setPixels(IntBuffer pixels) {
        this.pixels = pixels;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
