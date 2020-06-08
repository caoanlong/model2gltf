package com.threedcger.lib.gltf;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedcger.lib.gltf.model.BufferView;
import com.threedcger.lib.gltf.model.GlTF;
import com.threedcger.lib.gltf.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GltfUtils {
    static GlTF copy(GlTF gltf)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            objectMapper.writeValue(baos, gltf);
            return objectMapper.readValue(baos.toByteArray(), GlTF.class);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not copy glTF", e);
        }
    }

    /**
     * Creates a shallow copy of the given {@link BufferView}
     *
     * @param bufferView The {@link BufferView}
     * @return The copy
     */
    static BufferView copy(BufferView bufferView)
    {
        BufferView copy = new BufferView();
        copy.setExtensions(bufferView.getExtensions());
        copy.setExtras(bufferView.getExtras());
        copy.setName(bufferView.getName());
        copy.setBuffer(bufferView.getBuffer());
        copy.setByteOffset(bufferView.getByteOffset());
        copy.setByteLength(bufferView.getByteLength());
        copy.setTarget(bufferView.getTarget());
        copy.setByteStride(bufferView.getByteStride());
        return copy;
    }


    /**
     * Creates a shallow copy of the given {@link Image}
     *
     * @param image The {@link Image}
     * @return The copy
     */
    static Image copy(Image image)
    {
        Image copy = new Image();
        copy.setExtensions(image.getExtensions());
        copy.setExtras(image.getExtras());
        copy.setName(image.getName());
        copy.setUri(image.getUri());
        copy.setBufferView(image.getBufferView());
        copy.setMimeType(image.getMimeType());
        return copy;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private GltfUtils()
    {
        // Private constructor to prevent instantiation
    }
}
