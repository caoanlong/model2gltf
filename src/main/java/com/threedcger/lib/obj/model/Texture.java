package com.threedcger.lib.obj.model;

import lombok.Data;

import java.io.FileInputStream;
import java.nio.IntBuffer;

@Data
public class Texture {
    private Boolean transparent = false;
    private FileInputStream source;
    private String name;
    private String extension;
    private String path;
    private IntBuffer pixels;
    private Integer width;
    private Integer height;
}
