package com.threedcger.lib.gltf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.OutputStream;

public class GltfWriter {
    private boolean indenting;

    /**
     * Creates a new glTF writer. By default, the output written by this class
     * will be indented.
     */
    public GltfWriter()
    {
        this.indenting = true;
    }

    /**
     * Set whether the JSON output should be indented
     *
     * @param indenting whether the JSON output should be indented
     */
    public void setIndenting(boolean indenting)
    {
        this.indenting = indenting;
    }

    /**
     * Returns whether the JSON output will be indented
     *
     * @return Whether the JSON output will be indented
     */
    public boolean isIndenting()
    {
        return indenting;
    }

    /**
     * Write the given glTF to the given output stream. The caller
     * is responsible for closing the stream.
     *
     * @param gltf The glTF
     * @param outputStream The output stream
     * @throws IOException If an IO error occurred
     */
    public void write(Object gltf, OutputStream outputStream)
            throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        if (indenting)
        {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        objectMapper.writeValue(outputStream, gltf);
    }
}
