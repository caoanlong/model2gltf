package com.threedcger.lib.gltf;

import java.nio.ByteBuffer;

public class ImageModel {
    private String name;

    private String uri;
    private final String mimeType;
    private BufferViewModel bufferViewModel;
    private ByteBuffer imageData;

    public ImageModel(String mimeType, BufferViewModel bufferViewModel) {
        this.mimeType = mimeType;
        this.bufferViewModel = bufferViewModel;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public void setBufferViewModel(BufferViewModel bufferViewModel)
    {
        this.bufferViewModel = bufferViewModel;
    }

    public void setImageData(ByteBuffer imageData)
    {
        this.imageData = imageData;
    }

    public String getUri()
    {
        return uri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public BufferViewModel getBufferViewModel()
    {
        return bufferViewModel;
    }

    public ByteBuffer getImageData() {
        if (imageData == null) return bufferViewModel.getBufferViewData();
        return Buffers.createSlice(imageData);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
