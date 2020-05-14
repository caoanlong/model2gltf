package com.threedcger.lib.obj.model.gltf;

import lombok.Data;

import java.util.List;

@Data
public class BufferState {
    private List positionBuffers;
    private List normalBuffers;
    private List uvBuffers;
    private List indexBuffers;
    private List positionAccessors;
    private List normalAccessors;
    private List uvAccessors;
    private List indexAccessors;
}
